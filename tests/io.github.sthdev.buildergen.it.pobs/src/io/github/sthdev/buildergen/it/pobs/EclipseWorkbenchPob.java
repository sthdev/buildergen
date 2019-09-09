package io.github.sthdev.buildergen.it.pobs;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * This is a page-object for the Eclipse workbench.
 *
 */
public class EclipseWorkbenchPob {

	private static final SWTWorkbenchBot bot = new SWTWorkbenchBot();

	/**
	 * Closes the welcome screen that is shown when Eclipse starts. Skips if no
	 * welcome screen can be found.
	 */
	public void closeWelcomeScreen() {
		try {
			//Reduce SWTBot timeout for this operation to avoid unnecessary delays if no welcome screen is shown.
			bot.performWithTimeout(() -> bot.viewByTitle("Welcome").close(), 100);
		} catch (RuntimeException ex) {
			// Ignore. When executing UI tests with Tycho, no welcome screen is shown.
		}
	}

}
