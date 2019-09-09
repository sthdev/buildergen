package io.github.sthdev.buildergen.it.tests;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestBuilderGen {

	private static SWTBot bot;

	@BeforeClass
	public static void init() {
		bot = new SWTBot();
	}

	@Test
	public void ensureSaveIsDisabledWhenNothingIsDirty() {
		SWTBotMenu menu = bot.menu("File").menu("Save");

		assertThat("Save command in menu is not enabled", not(menu.isEnabled()));
	}

}
