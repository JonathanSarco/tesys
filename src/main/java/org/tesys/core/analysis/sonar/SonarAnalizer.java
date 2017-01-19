package org.tesys.core.analysis.sonar;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



import org.tesys.core.TesysPath;
import org.tesys.core.analysis.sonar.metricsdatatypes.Metrics;
import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.db.RevisionByOriginalIdentifierQuery;
import org.tesys.core.project.scm.RevisionPOJO;
import org.tesys.util.MD5;
import org.tesys.util.Searcher;

public class SonarAnalizer {

	private static final Logger LOG = Logger.getLogger(SonarAnalizer.class
			.getName());

	private static FileHandler handler;

	private SonarExtractor sonarExtractor;

	private static SonarAnalizer instance = null;

	private SonarAnalizer() {


		try {
			handler = new FileHandler(TesysPath.Path
					+ "logs"+ File.separator +"tesys-log.xml", 1024 * 1024, 10);
		} catch (SecurityException | IOException e) {
			LOG.log(Level.SEVERE, e.getMessage());
			handler = null;
		}
		LOG.addHandler(handler);

	}

	public static synchronized SonarAnalizer getInstance() {
		if (instance == null) {
			instance = new SonarAnalizer();
		}
		return instance;
	}

	/**
	 * Consigue todos los commits hechos y agarra los que todavia no fueron
	 * analizados por esta misma clase.
	 * 
	 * Una vez que tiene los no analizados, commit por commit, hace checkouts y
	 * ejecuta al sonar por cada checkout.
	 * 
	 * Luego extrae todos los datos desde la api timemachine de sonar y guarda
	 * en la base de datos que revisiones fueron analizadas como asi tambien los
	 * analisis
	 * 
	 * Devuelve true cuando se termina de hacer los analisis.
	 * 
	 * Esta clase trabaja en la carpeta .tesys del home por defecto, y espera
	 * que en dicha carpeta alla un archivo que sea pra ejecuatr sonar
	 * 
	 * Como asi tambien guarda los resultados de los checkouts en
	 * .tesys/workspace el archivo ant debe estar correctamente configurado para
	 * encontrar los codigos en ese lugar
	 * 
	 */
	public synchronized boolean executeSonarAnalysis(SonarAnalysisRequest sar) {

		/*
		 * se recuperan las rutas y se hacen checkouts y analisis de esas
		 * revisiones en esas rutas
		 */

		LOG.log(Level.INFO, "Se solicito una analisis de sonar");
		
		RevisionByOriginalIdentifierQuery daor =
				new RevisionByOriginalIdentifierQuery(sar.getRevision(), sar.getRepository());
		
		RevisionPOJO rev = daor.execute();
		
		if( rev == null ) {
			LOG.log(Level.INFO, "La revision no existe en el sistema");
			return false;
		}
		
		if( rev.isScaned() ) {
			LOG.log(Level.INFO, "La revision habia sido previamente escaneada");
			return false;
		}


		sonarExtractor = new SonarExtractor(sar.getUrl(), sar.getUrl(), sar.getPass());
		
		AnalisisPOJO analisisAcumulado = sonarExtractor.getResults(sar.getRevision(), sar.getRepository(), sar.getSonarKey());

		if( analisisAcumulado == null ) {
			return false;
		}
		
		//Se guarda el analisis por commit acumulado por si se quiere usar para otra cosa en el futuro
		ElasticsearchDao<AnalisisPOJO> daoac = new ElasticsearchDao<AnalisisPOJO>(
				AnalisisPOJO.class, ElasticsearchDao.DEFAULT_RESOURCE_ANALYSIS_COMMIT);
		
		daoac.create(analisisAcumulado.getID(), analisisAcumulado);
		
		
		List<SonarMetricPOJO> metricas = getMetrics();
		List<AnalisisPOJO> analisisPorCommit = getAnalisisPorCommit(
				analisisAcumulado, metricas);


		ElasticsearchDao<AnalisisPOJO> dao = new ElasticsearchDao<AnalisisPOJO>(
				AnalisisPOJO.class, ElasticsearchDao.DEFAULT_RESOURCE_ANALYSIS);

		List<AnalisisPOJO> analisisPorTareaAlmacenados = dao.readAll();
		
		List<AnalisisPOJO> analisisPorTarea = getAnalisisPorTarea(
				analisisPorCommit, analisisPorTareaAlmacenados, metricas);
		

		LOG.log(Level.INFO, "Se almacenaran los analisis de sonar");

		this.storeAnalysis(analisisPorTarea, dao);
		
		rev.setScaned(true);
		
		ElasticsearchDao<RevisionPOJO> daor2 = new ElasticsearchDao<RevisionPOJO>(
				RevisionPOJO.class, ElasticsearchDao.DEFAULT_RESOURCE_REVISION);
		
		daor2.update(rev.getID(), rev);
		
		return true;

	}

	public List<SonarMetricPOJO> getMetrics() {
		//Valores por default, nunca deberia ocurir el caso
		if( sonarExtractor == null ) {
			sonarExtractor = new SonarExtractor("http://localhost:9000/", "admin", "admin");
		}
		return sonarExtractor.getMetrics();
	}


	public void storeAnalysis(List<AnalisisPOJO> resultados,
			ElasticsearchDao<AnalisisPOJO> dao) {

		for (AnalisisPOJO analisis : resultados) {
			dao.create(analisis.getID(), analisis);
		}

	}

	public List<AnalisisPOJO> getAnalisisPorCommit(
			AnalisisPOJO analisiAcumulado,
			List<SonarMetricPOJO> metricas) {

		List<AnalisisPOJO> analisisPorCommit = new LinkedList<AnalisisPOJO>();

		ElasticsearchDao<AnalisisPOJO> ultimoAnalisisDAO = new ElasticsearchDao<AnalisisPOJO>(
				AnalisisPOJO.class,
				ElasticsearchDao.DEFAULT_RESOURCE_LAST_ANALYSIS);


		// se obtiune que path afecta para buscar un analisis anterior de
		// esa ruta
		String path = analisiAcumulado.getRevision().getAncestry();

		// los id de los ultimos analisis son el path en md5
		if (ultimoAnalisisDAO.exists(MD5.generateId(path))) {
			LOG.log(Level.INFO, "Se recupera analisis anterior para "
					+ path);

			AnalisisPOJO analisisAcumuladoPrevio = ultimoAnalisisDAO
					.read(MD5.generateId(path));

			analisisPorCommit.add(this.calcularDiferenciasDeAnalisis(
					analisisAcumuladoPrevio, analisiAcumulado,
					metricas));
			// si no existe un analisis previo de la ruta..
		} else {
				LOG.log(Level.INFO, "La revision en "+ path + "no pareciera tener un antencesor por lo que sera tomada como base para los siguintes analisis");
		}

		ultimoAnalisisDAO.update(MD5.generateId(analisiAcumulado.getRevision().getPath()),analisiAcumulado);
		
		return analisisPorCommit;
	}
	

	public AnalisisPOJO calcularDiferenciasDeAnalisis(
			AnalisisPOJO analisisAcumuladoPrevio,
			AnalisisPOJO analisisAcumuladoActual, List<SonarMetricPOJO> metricas) {

		// Creo un nuevo analisis que es correspondiente a la revision del
		// que se le van a restar valores
		AnalisisPOJO nuevoAnalisisPorCommit = new AnalisisPOJO(
				analisisAcumuladoActual.getRevision());

		LOG.log(Level.INFO, "Juntando los analisis de "
				+ analisisAcumuladoPrevio.getRevision().getRevision()
				+ analisisAcumuladoActual.getRevision().getRevision());

		List<KeyValuePOJO> resultadosPrevios = analisisAcumuladoPrevio
				.getResults();
		List<KeyValuePOJO> resultadosActuales = analisisAcumuladoActual
				.getResults();

		// Por cada una de los resultados (de la forma: "lines" = "10")
		for (int j = 0; j < resultadosActuales.size(); j++) {

			// Se crea un metricHandler, que es quien sabe que hacer con el
			// dato (caso de lineas es restar)
			// pero otro tipo de metricas requiere un tipo diferente de
			// diferencia
			Metrics metricHandler = null;
			// metric name es por ejemplo "lines"
			String metricName = resultadosActuales.get(j).getKey();
			// Los valores actuales y anteriores, por ejemplo 2 y 10, por lo
			// que el valor final debe ser 8
			String valorActual = resultadosActuales.get(j).getValue()
					.toString();
			String valorPrevio = resultadosPrevios.get(j).getValue().toString();

			// dado "lines" se obtiene toda la informacion de ese tipo de
			// metrica
			SonarMetricPOJO metric = Searcher
					.searchMetric(metricName, metricas);
			// En particular el tipo (que los define Sonar), el caso de
			// lines es INT
			// Y este tipo sirve para llamar el metricHandler apropiado
			String metricType = metric.getType();

			// Si la metrica vale "null" se descarta (nunca se almacena)
			// "profile" es un dato que se guarda que no es una metrica asi
			// que se ignora
			if (!"null".equals(valorActual) && !"profile".equals(metricName)
					&& !"profile_version".equals(metricName) 
					&& !"quality_profiles".equals(metricName)
					&& !"sqale_rating".equals(metricName)
					&& !"ncloc_language_distribution".equals(metricName)) {

				/*
				 * Se instancia metricHandler en el handler apropiado mediante
				 * refleccion Esto es, cada tipo de dato que define sonar para
				 * sus metricas (que se pueden ver en la api "/api/metrics")
				 * tiene una clase con el mismo nombre.
				 * 
				 * De esta forma la metrica lines que es de tipo INT (valor que
				 * toma metricType) va a ser un new de la clase INT en el
				 * paquete metric data type
				 * 
				 * Se puede implementar esta funcionalidad haciendo una cascada
				 * de if con news pero si sequiere agregar una nueva metric hay
				 * que modificar esta clase, de esta forma que solo agregar la
				 * clase nueva que sea el handler ya andaria.
				 */

				Object object = null;

				try {
					object = Class.forName(
							"org.tesys.core.analysis.sonar.metricsdatatypes"
									+ "." + metricType).getConstructors()[0]
							.newInstance(valorActual, valorPrevio);

					metricHandler = (Metrics) object;

					// Si se encontro un handler para ese tipo
					if (metricHandler != null) {
						// Se agrega una nueva metrica con valor por commit
						// al analisis por commit
						nuevoAnalisisPorCommit.add(new KeyValuePOJO(metricName,
								metricHandler.getDifferenceBetweenAnalysis()));

					}

				} catch (Exception e) {
					System.out.println(metricType + "--" + metricHandler + "--" + metricName);
					System.out.println(valorActual + "--" + valorPrevio );
					LOG.log(Level.SEVERE, e.toString(), e);
				}

			}

		}

		
		return nuevoAnalisisPorCommit;
	}

	/**
	 * Junta las metricas de uno o varios commits correspondientes a la misma
	 * tarea de jira
	 * 
	 * @param analisisJsonPorCommit
	 *            analisis por commit generador por esta misma clase
	 * @param revisiones
	 *            los datos de las revisiones generados por la clase
	 *            svnrevisions
	 * @return analisis por tarea de jira
	 */
	public List<AnalisisPOJO> getAnalisisPorTarea(
			List<AnalisisPOJO> analisisPorCommit,
			List<AnalisisPOJO> analisisPorTareaAlmacenados,
			List<SonarMetricPOJO> metricas) {

		List<AnalisisPOJO> analisisPorTareaGuardados = analisisPorTareaAlmacenados;

		for (AnalisisPOJO commitAnalisis : analisisPorCommit) {

			LOG.log(Level.INFO, "Procesando el analisis de "
					+ commitAnalisis.getRevision().getProjectTrackingTask());

			AnalisisPOJO guardado = Searcher.searchIssue(
					analisisPorTareaGuardados, commitAnalisis.getRevision()
							.getProjectTrackingTask());
			


			if (guardado == null) {
				// Si todavia no existe la tarea se guarda el analisis sin los
				// datos propios de un commit
				commitAnalisis.getRevision().setDate(0);
				commitAnalisis.getRevision().setRevision(null);
				commitAnalisis.getRevision().setRepository(null);
				analisisPorTareaGuardados.add(commitAnalisis);
			} else {

				AnalisisPOJO nuevoAnalisisPorTarea = new AnalisisPOJO(
						guardado.getRevision());

				List<KeyValuePOJO> resultadosPrevios = guardado.getResults();
				List<KeyValuePOJO> resultadosActuales = commitAnalisis
						.getResults();
				
				analisisPorTareaGuardados.remove(guardado);

				for (int j = 0; j < resultadosActuales.size(); j++) {

					Metrics metricHandler = null;
					String metricName = resultadosActuales.get(j).getKey();
					Double valorActual = (Double) resultadosActuales.get(j)
							.getValue();

					Double valorPrevio = Searcher.searchMetricValue(
							resultadosPrevios, metricName);

					if (valorPrevio != null) {
						SonarMetricPOJO metric = Searcher.searchMetric(
								metricName, metricas);
						String metricType = metric.getType();

						Object object = null;

						try {
							object = Class.forName(
									"org.tesys.core.analysis.sonar.metricsdatatypes"
											+ "." + metricType)
									.getConstructors()[1].newInstance(
									valorActual, valorPrevio);

						} catch (Exception e) {
							System.out.println(valorActual + " " + valorPrevio);

							LOG.log(Level.SEVERE, e.toString(), e);
						}

						metricHandler = (Metrics) object;

						if (metricHandler != null) {
							nuevoAnalisisPorTarea.add(new KeyValuePOJO(
									metricName, metricHandler
											.getNewAnalysisPerTask()));

						}

					}

				}
				analisisPorTareaGuardados.add(nuevoAnalisisPorTarea);

			}

		}

		return analisisPorTareaGuardados;
	}

}
