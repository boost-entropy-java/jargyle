package com.github.jh3nd3rs0n.jargyle.cli;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;
import com.github.jh3nd3rs0n.jargyle.FilesHelper;
import com.github.jh3nd3rs0n.jargyle.IoHelper;
import com.github.jh3nd3rs0n.jargyle.TestResourceConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;

public class ServerConfigurationFileCreatorCLIIT {
	
	private Path baseDir = null;
	private Path combinedConfigurationFile = null;
	private Path configurationFile = null;
	private Path emptyConfigurationFile = null;
	private Path supplementedConfigurationFile = null;
	
	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		this.combinedConfigurationFile = this.baseDir.resolve("combined_configuration.xml");
		this.configurationFile = this.baseDir.resolve("configuration.xml");
		this.emptyConfigurationFile = this.baseDir.resolve("empty_configuration.xml");
		this.supplementedConfigurationFile = this.baseDir.resolve("supplemented_configuration.xml");		
	}

	@After
	public void tearDown() throws Exception {
		if (this.supplementedConfigurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.supplementedConfigurationFile);
			this.supplementedConfigurationFile = null;
		}
		if (this.emptyConfigurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.emptyConfigurationFile);
			this.emptyConfigurationFile = null;
		}
		if (this.configurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.configurationFile);
			this.configurationFile = null;
		}
		if (this.combinedConfigurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.combinedConfigurationFile);
			this.combinedConfigurationFile = null;
		}
		if (this.baseDir != null) {
			FilesHelper.attemptsToDeleteIfExists(this.baseDir);
			this.baseDir = null;
		}
	}

	@Test
	public void testMainForCombiningConfigurationFiles() throws IOException {
		String[] args = new String[] {
				"--config-file=".concat(
						TestResourceConstants.JARGYLE_SERVER_CONFIGREPO_IMPL_CONFIGURATION_FILE.getFile().getAbsolutePath()),
				"--config-file=".concat(
						TestResourceConstants.JARGYLE_SERVER_CONFIGREPO_IMPL_SUPPLEMENTED_CONFIGURATION_FILE.getFile().getAbsolutePath()),
				this.combinedConfigurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		ThreadHelper.sleepForThreeSeconds();		
		String expectedCombinedConfigurationFileContents =
				TestResourceConstants.JARGYLE_SERVER_CONFIGREPO_IMPL_COMBINED_CONFIGURATION_FILE.getContentAsString().replace(
						"\r\n", "\n").trim();
		String actualCombinedConfigurationFileContents =
				IoHelper.readStringFrom(this.combinedConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedCombinedConfigurationFileContents, 
				actualCombinedConfigurationFileContents);
	}

	@Test
	public void testMainForCreatingAConfigurationFile() throws IOException {
		String[] args = new String[] {
				"--setting=port=1234",
				"--setting=backlog=100",
				"--setting=socks5.methods=NO_AUTHENTICATION_REQUIRED",
				this.configurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		ThreadHelper.sleepForThreeSeconds();		
		String expectedConfigurationFileContents =
				TestResourceConstants.JARGYLE_SERVER_CONFIGREPO_IMPL_CONFIGURATION_FILE.getContentAsString().replace(
						"\r\n", "\n").trim();
		String actualConfigurationFileContents =
				IoHelper.readStringFrom(this.configurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedConfigurationFileContents, 
				actualConfigurationFileContents);
	}

	@Test
	public void testMainForCreatingAnEmptyConfigurationFile() throws IOException {
		String[] args = new String[] {
				this.emptyConfigurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		ThreadHelper.sleepForThreeSeconds();		
		String expectedEmptyConfigurationFileContents =
				TestResourceConstants.JARGYLE_SERVER_CONFIGREPO_IMPL_EMPTY_CONFIGURATION_FILE.getContentAsString().replace(
						"\r\n", "\n").trim();
		String actualEmptyConfigurationFileContents =
				IoHelper.readStringFrom(this.emptyConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedEmptyConfigurationFileContents, 
				actualEmptyConfigurationFileContents);
	}
	
	@Test
	public void testMainForSupplementingAConfigurationFile() throws IOException {
		String[] args = new String[] {
				"--config-file=".concat(
						TestResourceConstants.JARGYLE_SERVER_CONFIGREPO_IMPL_CONFIGURATION_FILE.getFile().getAbsolutePath()),
				"--setting=socksServerSocketSettings=SO_TIMEOUT=0",
				this.supplementedConfigurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		ThreadHelper.sleepForThreeSeconds();		
		String expectedSupplementedConfigurationFileContents =
				TestResourceConstants.JARGYLE_SERVER_CONFIGREPO_IMPL_SUPPLEMENTED_CONFIGURATION_FILE.getContentAsString().replace(
						"\r\n", "\n").trim();
		String actualSupplementedConfigurationFileContents =
				IoHelper.readStringFrom(this.supplementedConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedSupplementedConfigurationFileContents, 
				actualSupplementedConfigurationFileContents);
	}

}
