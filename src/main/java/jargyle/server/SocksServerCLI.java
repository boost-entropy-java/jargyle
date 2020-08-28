package jargyle.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import argmatey.ArgMatey;
import argmatey.ArgMatey.Annotations.Ignore;
import argmatey.ArgMatey.Annotations.Option;
import argmatey.ArgMatey.Annotations.OptionArgSpec;
import argmatey.ArgMatey.Annotations.Ordinal;
import argmatey.ArgMatey.CLI;
import argmatey.ArgMatey.IllegalOptionArgException;
import argmatey.ArgMatey.OptionType;
import jargyle.client.Scheme;
import jargyle.client.socks5.DefaultUsernamePasswordRequestor;
import jargyle.client.socks5.UsernamePassword;
import jargyle.client.socks5.UsernamePasswordRequestor;
import jargyle.common.cli.HelpTextParams;
import jargyle.common.net.SocketSettingSpec;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevel;
import jargyle.server.socks5.UsernamePasswordAuthenticator;
import jargyle.server.socks5.UsersCLI;

public final class SocksServerCLI extends CLI {
	
	private static final int ALLOWED_CLIENT_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 0;
	private static final int ALLOWED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 1;
	private static final int ALLOWED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 2;
	private static final int BLOCKED_CLIENT_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 3;
	private static final int BLOCKED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 4;
	private static final int BLOCKED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 5;
	private static final int CONFIG_FILE_OPTION_GROUP_ORDINAL = 6;
	private static final int CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL = 7;
	private static final int ENTER_EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_GROUP_ORDINAL = 8;
	private static final int EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_GROUP_ORDINAL = 9;
	private static final int HELP_OPTION_GROUP_ORDINAL = 10;
	private static final int MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL = 11;
	private static final int NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL = 12;
	private static final int SETTINGS_HELP_OPTION_GROUP_ORDINAL = 13;
	private static final int SETTINGS_OPTION_GROUP_ORDINAL = 14;
	private static final int SOCKS5_USER_PASS_AUTHENTICATOR_OPTION_GROUP_ORDINAL = 15;
	private static final int SOCKS5_USERS_OPTION_GROUP_ORDINAL = 16;

	private static final String CRITERIA_OPTION_USAGE = 
			"${option}=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"; 
	
	private static final Logger LOGGER = Logger.getLogger(
			SocksServerCLI.class.getName());
	
	private boolean configurationFileXsdRequested;
	private final ModifiableConfiguration modifiableConfiguration;
	private String monitoredConfigurationFile;
	private boolean newConfigurationFileRequested;
	private final boolean posixlyCorrect;
	private final String programBeginningUsage;
	private boolean settingsHelpDisplayed;
	private Integer socks5UsersManagementModeStatus;
	
	SocksServerCLI(final String[] args, final boolean posixCorrect) {
		super(args, posixCorrect);
		String progName = System.getProperty(
				SystemPropertyNameConstants.PROGRAM_NAME);
		if (progName == null) {
			progName = SocksServer.class.getName();
		}
		String progBeginningUsage = System.getProperty(
				SystemPropertyNameConstants.PROGRAM_BEGINNING_USAGE);
		if (progBeginningUsage == null) {
			progBeginningUsage = progName;
		}
		this.configurationFileXsdRequested = false;
		this.modifiableConfiguration = new ModifiableConfiguration();
		this.monitoredConfigurationFile = null;
		this.newConfigurationFileRequested = false;
		this.posixlyCorrect = posixCorrect;
		this.programBeginningUsage = progBeginningUsage;
		this.programName = progName;
		this.settingsHelpDisplayed = false;
		this.socks5UsersManagementModeStatus = null;
	}
		
	@Option(
			doc = "The space separated list of allowed client address "
					+ "criteria",
			name = "allowed-client-addr-criteria",
			type = OptionType.GNU_LONG,
			usage = CRITERIA_OPTION_USAGE
	)
	@Ordinal(ALLOWED_CLIENT_ADDR_CRITERIA_OPTION_GROUP_ORDINAL)
	public void addAllowedClientAddressCriteria(
			final Criteria allowedClientAddrCriteria) {
		this.modifiableConfiguration.addAllowedClientAddressCriteria(
				allowedClientAddrCriteria);
	}
		
	@Option(
			doc = "The space separated list of allowed SOCKS5 "
					+ "incoming TCP address criteria",
			name = "allowed-socks5-incoming-tcp-addr-criteria",
			type = OptionType.GNU_LONG,
			usage = CRITERIA_OPTION_USAGE
	)
	@Ordinal(ALLOWED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL)
	public void addAllowedSocks5IncomingTcpAddressCriteria(
			final Criteria allowedSocks5IncomingTcpAddrCriteria) {
		this.modifiableConfiguration.addAllowedSocks5IncomingTcpAddressCriteria(
				allowedSocks5IncomingTcpAddrCriteria);
	}
		
	@Option(
			doc = "The space separated list of allowed SOCKS5 "
					+ "incoming UDP address criteria",
			name = "allowed-socks5-incoming-udp-addr-criteria",
			type = OptionType.GNU_LONG,
			usage = CRITERIA_OPTION_USAGE
	)
	@Ordinal(ALLOWED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL)
	public void addAllowedSocks5IncomingUdpAddressCriteria(
			final Criteria allowedSocks5IncomingUdpAddrCriteria) {
		this.modifiableConfiguration.addAllowedSocks5IncomingUdpAddressCriteria(
				allowedSocks5IncomingUdpAddrCriteria);
	}

	@Option(
			doc = "The space separated list of blocked client address "
					+ "criteria",
			name = "blocked-client-addr-criteria",
			type = OptionType.GNU_LONG,
			usage = CRITERIA_OPTION_USAGE
	)
	@Ordinal(BLOCKED_CLIENT_ADDR_CRITERIA_OPTION_GROUP_ORDINAL)
	public void addBlockedClientAddressCriteria(
			final Criteria blockedClientAddrCriteria) {
		this.modifiableConfiguration.addBlockedClientAddressCriteria(
				blockedClientAddrCriteria);
	}
	
	@Option(
			doc = "The space separated list of blocked SOCKS5 "
					+ "incoming TCP address criteria",
			name = "blocked-socks5-incoming-tcp-addr-criteria",
			type = OptionType.GNU_LONG,
			usage = CRITERIA_OPTION_USAGE
	)
	@Ordinal(BLOCKED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL)
	public void addBlockedSocks5IncomingTcpAddressCriteria(
			final Criteria blockedSocks5IncomingTcpAddrCriteria) {
		this.modifiableConfiguration.addBlockedSocks5IncomingTcpAddressCriteria(
				blockedSocks5IncomingTcpAddrCriteria);
	}
		
	@Option(
			doc = "The space separated list of blocked SOCKS5 "
					+ "incoming UDP address criteria",
			name = "blocked-socks5-incoming-udp-addr-criteria",
			type = OptionType.GNU_LONG,
			usage = CRITERIA_OPTION_USAGE
	)
	@Ordinal(BLOCKED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL)
	public void addBlockedSocks5IncomingUdpAddressCriteria(
			final Criteria blockedSocks5IncomingUdpAddrCriteria) {
		this.modifiableConfiguration.addBlockedSocks5IncomingUdpAddressCriteria(
				blockedSocks5IncomingUdpAddrCriteria);
	}
		
	@Option(
			doc = "The configuration file",
			name = "config-file",
			optionArgSpec = @OptionArgSpec(name = "FILE"),
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "f",
			type = OptionType.POSIX
	)
	@Ordinal(CONFIG_FILE_OPTION_GROUP_ORDINAL)
	public void addConfigurationFile(final String file)	throws IOException {
		InputStream in = null;
		if (file.equals("-")) {
			in = System.in;
		} else {
			File f = new File(file);
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}
		Configuration configuration = null;
		try {
			configuration = ImmutableConfiguration.newInstanceFrom(in);
		} catch (JAXBException e) { 
			throw new IllegalArgumentException(String.format(
					"possible invalid XML file '%s'", file), 
					e);
		}  finally {
			if (in instanceof FileInputStream) {
				in.close();
			}
		}
		this.modifiableConfiguration.add(configuration);
	}
		
	@Option(
			doc = "The comma separated list of settings for the SOCKS "
					+ "server",
			name = "settings",
			type = OptionType.GNU_LONG,
			usage = "${option}=[NAME1=VALUE1[,NAME2=VALUE2[...]]]"
	)
	@Option(
			name = "s",
			type = OptionType.POSIX,
			usage = "${option} [NAME1=VALUE1[,NAME2=VALUE2[...]]]"
	)
	@Ordinal(SETTINGS_OPTION_GROUP_ORDINAL)
	public void addSettings(final Settings sttngs) {
		this.modifiableConfiguration.addSettings(sttngs);
	}
	
	private void displayHelpText(final List<HelpTextParams> list) {
		System.out.println();
		for (HelpTextParams helpTextParams : list) {
			System.out.print("  ");
			System.out.println(helpTextParams.getUsage());
			System.out.print("      ");
			System.out.println(helpTextParams.getDoc());
			System.out.println();
		}
	}
		
	@Option(
			doc = "Print this help and exit",
			name = "help",
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "h",
			type = OptionType.POSIX
	)
	@Ordinal(HELP_OPTION_GROUP_ORDINAL)
	@Override
	public void displayProgramHelp() {
		ArgMatey.Option configFileXsdOption = this.getOptionGroups().get(
				CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option monitoredConfigFileOption = this.getOptionGroups().get(
				MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option newConfigFileOption = this.getOptionGroups().get(
				NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option settingsHelpOption = this.getOptionGroups().get(
				SETTINGS_HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option socks5UsersOption = this.getOptionGroups().get(
				SOCKS5_USERS_OPTION_GROUP_ORDINAL).get(0);
		System.out.printf("Usage: %s [OPTIONS]%n", this.programBeginningUsage);
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				configFileXsdOption.getUsage());
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				monitoredConfigFileOption.getUsage());		
		System.out.printf("       %s [OPTIONS] %s%n", 
				this.programBeginningUsage, 
				newConfigFileOption.getUsage());
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				settingsHelpOption.getUsage());
		System.out.printf("       %s %s ARGS", 
				this.programBeginningUsage, 
				socks5UsersOption.getUsage());
		System.out.println();
		System.out.println();
		System.out.println("OPTIONS:");
		this.getOptionGroups().printHelpText();
		System.out.println();
		this.programHelpDisplayed = true;
	}
	
	@Ignore
	@Override
	public void displayProgramVersion() { 
		throw new UnsupportedOperationException("not implemented");
	}
		
	@Option(
			doc = "Print the list of available settings for the SOCKS "
					+ "server and exit",
			name = "settings-help",
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "H",
			type = OptionType.POSIX
	)
	@Ordinal(SETTINGS_HELP_OPTION_GROUP_ORDINAL)
	public void displaySettingsHelp() {
		System.out.println("SETTINGS:");
		this.displayHelpText(Arrays.asList(SettingSpec.values()));
		System.out.println("SCHEMES:");
		this.displayHelpText(Arrays.asList(Scheme.values()));
		System.out.println("SOCKET_SETTINGS:");
		this.displayHelpText(Arrays.asList(SocketSettingSpec.values()));
		System.out.println("SOCKS5_AUTH_METHODS:");
		this.displayHelpText(Arrays.asList(AuthMethod.values()));
		System.out.println("SOCKS5_GSSAPI_PROTECTION_LEVELS:");
		this.displayHelpText(Arrays.asList(GssapiProtectionLevel.values()));
		this.settingsHelpDisplayed = true;
	}
	
	@Option(
			doc = "Mode for managing SOCKS5 users (add --help for "
					+ "more information)",
			name = "socks5-users",
			type = OptionType.GNU_LONG
	)
	@Ordinal(SOCKS5_USERS_OPTION_GROUP_ORDINAL)
	public void doSocks5UsersManagementMode() {
		ArgMatey.Option socks5UsersOption = this.getOptionGroups().get(
				SOCKS5_USERS_OPTION_GROUP_ORDINAL).get(0);
		String newProgramBeginningUsage = String.format("%s %s", 
				this.programBeginningUsage, 
				socks5UsersOption.getUsage());
		List<String> remainingArgList = new ArrayList<String>();
		while (this.hasNext()) {
			String arg = this.next();
			remainingArgList.add(arg);
		}
		String[] remainingArgs = remainingArgList.toArray(
				new String[remainingArgList.size()]);
		UsersCLI usersCLI = new UsersCLI(
				this.programName, 
				newProgramBeginningUsage, 
				remainingArgs, 
				this.posixlyCorrect);
		int status = usersCLI.handleRemaining();
		this.socks5UsersManagementModeStatus = Integer.valueOf(status);
	}
		
	@Option(
			doc = "Enter through an interactive prompt the username "
					+ "password to be used to access the external "
					+ "SOCKS5 server used for external connections",
			name = "enter-external-client-socks5-user-pass",
			type = OptionType.GNU_LONG
	)
	@Ordinal(ENTER_EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_GROUP_ORDINAL)
	public void enterExternalClientSocks5UsernamePassword() {
		String prompt = "Please enter username and password for the external "
				+ "SOCKS5 server for external connections";
		UsernamePasswordRequestor usernamePasswordRequestor = 
				new DefaultUsernamePasswordRequestor();
		UsernamePassword usernamePassword = 
				usernamePasswordRequestor.requestUsernamePassword(null, prompt);
		this.modifiableConfiguration.setExternalClientSocks5UsernamePassword(
				usernamePassword);
	}
	
	public int handleRemaining() {
		ArgMatey.OptionGroup settingsOptionGroup = this.getOptionGroups().get(
				SETTINGS_OPTION_GROUP_ORDINAL); 
		ArgMatey.Option settingsHelpOption = this.getOptionGroups().get(
				SETTINGS_HELP_OPTION_GROUP_ORDINAL).get(0);
		String settingsHelpSuggestion = String.format(
				"Try `%s %s' for more information.", 
				this.programBeginningUsage, 
				settingsHelpOption.getUsage());
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		String suggestion = String.format(
				"Try `%s %s' for more information.", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		while (this.hasNext()) {
			try {
				this.handleNext();
			} catch (IllegalOptionArgException e) {
				String suggest = suggestion;
				if (settingsOptionGroup.toList().contains(e.getOption())) {
					suggest = settingsHelpSuggestion;
				}
				System.err.printf("%s: %s%n", this.programName, e);
				System.err.println(suggest);
				e.printStackTrace(System.err);
				return -1;
			} catch (Throwable t) {
				System.err.printf("%s: %s%n", this.programName, t);
				System.err.println(suggestion);
				t.printStackTrace(System.err);
				return -1;
			}
			if (this.configurationFileXsdRequested
					|| this.newConfigurationFileRequested
					|| this.programHelpDisplayed
					|| this.settingsHelpDisplayed) {
				return 0;
			}
			if (this.socks5UsersManagementModeStatus != null) {
				return this.socks5UsersManagementModeStatus.intValue();
			}
		}
		Configuration configuration = this.newConfiguration();
		if (configuration == null) { return -1;	}
		return this.startSocksServer(configuration);
	}
	
	private Configuration newConfiguration() {
		Configuration configuration = null;
		if (this.monitoredConfigurationFile == null) {
			configuration = ImmutableConfiguration.newInstance(
					this.modifiableConfiguration);
		} else {
			File f = new File(this.monitoredConfigurationFile);
			ConfigurationService configurationService = null;
			try {
				configurationService = 
						XmlFileSourceConfigurationService.newInstance(f);
			} catch (IllegalArgumentException e) {
				System.err.printf("%s: %s%n", this.programName, e);
				e.printStackTrace(System.err);
				return null;
			}
			configuration = new MutableConfiguration(configurationService);
		}
		return configuration;
	}
		
	@Option(
			doc = "Create a new configuration file based on the "
					+ "preceding options and exit",
			name = "new-config-file",
			optionArgSpec = @OptionArgSpec(name = "FILE"),
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "n",
			type = OptionType.POSIX
	)
	@Ordinal(NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL)
	public void newConfigurationFile(final String file) 
			throws JAXBException, IOException {
		ImmutableConfiguration immutableConfiguration = 
				ImmutableConfiguration.newInstance(
						this.modifiableConfiguration);
		String tempArg = file;
		System.out.print("Writing to ");
		OutputStream out = null;
		if (file.equals("-")) {
			System.out.printf("standard output...%n");
			out = System.out;
		} else {
			File f = new File(file);
			System.out.printf("'%s'...%n", f.getAbsolutePath());
			File tempFile = null;
			do {
				tempArg = tempArg.concat(".tmp");
				tempFile = new File(tempArg);
			} while (tempFile.exists());
			tempFile.createNewFile();
			out = new FileOutputStream(tempFile);
		}
		try {
			byte[] xml = immutableConfiguration.toXml();
			out.write(xml);
			out.flush();
		} finally {
			if (out instanceof FileOutputStream) {
				out.close();
			}
		}
		if (!file.equals("-")) {
			File f = new File(file);
			File tempFile = new File(tempArg);
			if (!tempFile.renameTo(f)) {
				throw new IOException(String.format(
						"unable to rename '%s' to '%s'", tempFile, f));
			}
		}
		this.newConfigurationFileRequested = true;
	}
	
	@Option(
			doc = "Print the configuration file XSD and exit",
			name = "config-file-xsd",
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "x",
			type = OptionType.POSIX
	)
	@Ordinal(CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL)
	public void printConfigurationFileXsd() throws JAXBException, IOException {
		byte[] xsd = ImmutableConfiguration.getXsd();
		System.out.write(xsd);
		System.out.flush();
		this.configurationFileXsdRequested = true;
	}
	
	@Option(
			doc = "The username password to be used to access the "
					+ "external SOCKS5 server used for external "
					+ "connections",
			name = "external-client-socks5-user-pass",
			type = OptionType.GNU_LONG,
			usage = "${option}=USERNAME:PASSWORD"
	)
	@Ordinal(EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_GROUP_ORDINAL)
	public void setExternalClientSocks5UsernamePassword(
			final UsernamePassword usernamePassword) {
		this.modifiableConfiguration.setExternalClientSocks5UsernamePassword(
				usernamePassword);
	}
		
	@Option(
			doc = "The configuration file to be monitored for any "
					+ "changes to be applied to the running "
					+ "configuration",
			name = "monitored-config-file",
			optionArgSpec = @OptionArgSpec(name = "FILE"),
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "m",
			type = OptionType.POSIX
	)
	@Ordinal(MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL)
	public void setMonitoredConfigurationFile(final String file) {
		this.monitoredConfigurationFile = file;
	}
		
	@Option(
			doc = "The SOCKS5 username password authenticator for the "
					+ "SOCKS server",
			name = "socks5-user-pass-authenticator",
			type = OptionType.GNU_LONG,
			usage = "${option}=CLASSNAME[:VALUE]"
	)
	@Ordinal(SOCKS5_USER_PASS_AUTHENTICATOR_OPTION_GROUP_ORDINAL)
	public void setSocks5UsernamePasswordAuthenticator(
			final UsernamePasswordAuthenticator usernamePasswordAuthenticator) {
		this.modifiableConfiguration.setSocks5UsernamePasswordAuthenticator(
				usernamePasswordAuthenticator);
	}
	
	private int startSocksServer(final Configuration configuration) {
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
		} catch (BindException e) {
			LOGGER.log(
					Level.SEVERE, 
					String.format(
							"Unable to listen on port %s at %s", 
							configuration.getSettings().getLastValue(
									SettingSpec.PORT, Port.class),
							configuration.getSettings().getLastValue(
									SettingSpec.HOST, Host.class)), 
					e);
			return -1;
		} catch (IOException e) {
			LOGGER.log(
					Level.SEVERE, 
					"Error in starting SocksServer", 
					e);
			return -1;
		}
		LOGGER.log(
				Level.INFO,
				String.format(
						"Listening on port %s at %s",
						socksServer.getPort(),
						socksServer.getHost()));
		return 0;
	}

}