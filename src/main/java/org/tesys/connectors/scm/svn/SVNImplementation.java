package org.tesys.connectors.scm.svn;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tesys.core.TesysPath;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc2.SvnLog;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnRevisionRange;
import org.tmatesoft.svn.core.wc2.SvnTarget;

public class SVNImplementation {

	private static String BRANCHPATTERN = "(.*/branches/.*?/trunk(/?))";

	private static final Logger LOG = Logger.getLogger(SVNConnector.class
			.getName());

	private static FileHandler handler;

	private static SVNImplementation instance = null;

	private SVNImplementation() {
		try {
			handler = new FileHandler(TesysPath.Path + "logs" + File.separator
					+ "tesys-log.xml", 1024 * 1024, 10);
		} catch (SecurityException | IOException e) {
			LOG.log(Level.SEVERE, e.getMessage());
			handler = null;
		}
		LOG.addHandler(handler);

	}

	public static SVNImplementation getInstance() {
		if (instance == null) {
			instance = new SVNImplementation();
		}
		return instance;
	}

	public long checkout(SVNURL url, SVNRevision revision, File destPath,
			boolean isRecursive) throws SVNException {
		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		updateClient.setIgnoreExternals(false);

		LOG.log(Level.INFO, "Realizando checkout de " + url.toString() + " -> "
				+ revision.toString());

		return updateClient.doCheckout(url, destPath, revision, revision,
				SVNDepth.INFINITY, false);
	}

	/**
	 * Devuelve una lista de paths, archivos modificados eliminados o agregados
	 */
	public String diff(String url, Integer initRevision, Integer lastRevision) {
		final List<String> ret = new LinkedList<String>();
		SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		SVNDiffClient diffClient = new SVNDiffClient(svnOperationFactory);
		LOG.log(Level.INFO, "Realizando diff de " + url.toString() + " -> "
				+ initRevision.toString() + ":" + lastRevision.toString());
		try {
			diffClient.doDiffStatus(SVNURL.parseURIEncoded(url),
					SVNRevision.create(initRevision),
					SVNURL.parseURIEncoded(url),
					SVNRevision.create(lastRevision), SVNDepth.INFINITY, false,
					new ISVNDiffStatusHandler() {
						public void handleDiffStatus(SVNDiffStatus diffStatus)
								throws SVNException {
							if (diffStatus.getKind() == SVNNodeKind.FILE) {
									ret.add(diffStatus.getFile().getPath());
							}
						}
					});
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.toString(), e);
		}
		
		StringBuilder result = new StringBuilder();
		for (String string : ret) {
			result.append(string);
			result.append("\n");
		}
		
		return result.toString();
	}

	/**
	 * Devuelve una lista de paths, archivos modificados eliminados o agregados
	 */
	public String diff2(final String url, final Integer initRevision,
			final Integer lastRevision) {

		LOG.log(Level.INFO, "Realizando diff de " + url.toString() + " -> "
				+ initRevision.toString() + ":" + lastRevision.toString());

		final StringBuffer string = new StringBuffer();

		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
			public Object call() {

				SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
				SVNDiffClient diffClient = new SVNDiffClient(
						svnOperationFactory);

				try {
					diffClient.doDiff(SVNURL.parseURIEncoded(url),
							SVNRevision.create(initRevision),
							SVNURL.parseURIEncoded(url),
							SVNRevision.create(lastRevision),
							SVNDepth.INFINITY, false, new OutputStream() {
								@Override
								public void write(int b) throws IOException {
									string.append((char) b);
									if (Thread.currentThread().isInterrupted()) {
										throw new IOException();
									}
								}
							});
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}

				return string.toString();
			}
		};

		Future<Object> future = executor.submit(task);
		Object result = "";
		try {
			result = future.get(2, TimeUnit.MINUTES);
		} catch (TimeoutException ex) {
			return string.toString();
		} catch (InterruptedException e) {
			// handle the interrupts
		} catch (ExecutionException e) {
			// handle other exceptions
		} finally {
			future.cancel(true);
			executor.shutdownNow();
			executor.shutdown();
		}

		return result.toString();

	}

	/**
	 * Dada una revision y la url de un repositorio, este metodo calcula el
	 * branch/trunk donde se realizo el commit, para luego hacer el analisis de
	 * dicho branch y no de todo el repositorio.
	 * 
	 * @param url
	 *            direccion del repositorio
	 * @param rev
	 *            revision que genero el commit.
	 * @return url base del branch/trunk donde se hizo el commit.
	 */
	public String getSvnBasePath(String url, int rev) {
		String changedPath = "/";

		LOG.log(Level.INFO, "Calculando ruta de " + url + " en " + rev);

		// from 'stackoverflow.com/questions/11029968/svn-log-using-svnkit'
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final SvnLog log = svnOperationFactory.createLog();
		try {
			log.setSingleTarget(SvnTarget.fromURL(SVNURL.parseURIEncoded(url)));
			log.addRange(SvnRevisionRange.create(SVNRevision.create(rev),
					SVNRevision.create(rev)));
			log.setDiscoverChangedPaths(true);
			final SVNLogEntry logEntry = log.run();
			final Map<String, SVNLogEntryPath> changedPaths = logEntry
					.getChangedPaths();
			for (Map.Entry<String, SVNLogEntryPath> entry : changedPaths
					.entrySet()) {
				changedPath = entry.getValue().getPath();
				break;
			}

			return evaluateRegex(url, changedPath);

		} catch (SVNException e) {
			LOG.log(Level.SEVERE, e.toString(), e);
		}
		return null;

	}

	private String evaluateRegex(String url, String changedPath) {

		String basePath = null;
		if (!"/".equals(changedPath)) {
			// Primero me fijo si esta en un trunk, si no machea con ningun
			// trunk entonces esta si o si en un branch.
			Pattern trunkPattern = Pattern.compile(BRANCHPATTERN);
			Matcher m = trunkPattern.matcher(changedPath);
			if (m.find()) {
				basePath = m.group(0);
			} else {
				// no corresponde (se hizo commit obre un lugar que no se
				// analiza)
				// como el trunk o un branch de branch o tag
				return null;
			}
			basePath = url + basePath;
			basePath = basePath.replaceAll("//", "/");
			basePath = basePath.replaceAll(":/", "://");
			return basePath;

		}
		return null;
	}

	public SvnPathRevisionPOJO getAncestry(String url, int rev) {

		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final SvnLog log = svnOperationFactory.createLog();
		try {
			log.setSingleTarget(SvnTarget.fromURL(SVNURL.parseURIEncoded(url)));
		} catch (SVNException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
		log.addRange(SvnRevisionRange.create(SVNRevision.create(rev),
				SVNRevision.create(rev)));
		log.setDiscoverChangedPaths(true);
		SVNLogEntry logEntry = null;
		try {
			logEntry = log.run();
		} catch (SVNException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
		SvnPathRevisionPOJO ret = null;
		final Map<String, SVNLogEntryPath> changedPaths = logEntry
				.getChangedPaths();
		for (Map.Entry<String, SVNLogEntryPath> entry : changedPaths.entrySet()) {

			final SVNLogEntryPath svnLogEntryPath = entry.getValue();

			String path = svnLogEntryPath.getCopyPath();
			if (path != null) {

				Integer lastRev = (int) svnLogEntryPath.getCopyRevision();
				path = evaluateRegex(url, path);

				ret = new SvnPathRevisionPOJO(path, lastRev, rev);
			}

			// los cambios no provienen de otro branch
			if (ret == null) {
				path = evaluateRegex(url, entry.getValue().getPath());

				ret = new SvnPathRevisionPOJO(path, rev - 1, rev);

			}

		}

		return ret;
	}

}
