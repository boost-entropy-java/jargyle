package jargyle.server;

import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.OptionArgSpec;
import argmatey.ArgMatey.OptionUsageParams;
import argmatey.ArgMatey.OptionUsageProvider;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.PosixOption;
import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class SocksServerCliOptions extends Options {
	
	public static final Option CONFIG_FILE_OPTION = new GnuLongOption.Builder(
			"config-file")
			.doc("The configuration file")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("FILE")
					.build())
			.ordinal(0)
			.otherBuilders(new PosixOption.Builder('f'))
			.build();
	
	public static final Option CONFIG_FILE_XSD_OPTION = 
			new GnuLongOption.Builder("config-file-xsd")
			.doc("Print the configuration file XSD and exit")
			.ordinal(1)
			.otherBuilders(new PosixOption.Builder('x'))
			.special(true)
			.build();
	
	public static final Option ENTER_EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION =
			new GnuLongOption.Builder("enter-external-client-socks5-user-pass")
			.doc("Enter through an interactive prompt the username password "
					+ "for the external SOCKS5 server for external connections")
			.ordinal(2)
			.build();
	
	public static final Option EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION =
			new GnuLongOption.Builder("external-client-socks5-user-pass")
			.doc("The username password for the external SOCKS5 server for "
					+ "external connections")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("USERNAME_PASSWORD")
					.type(UsernamePassword.class)
					.build())
			.optionUsageProvider(new OptionUsageProvider() {

				@Override
				public String getOptionUsage(final OptionUsageParams params) {
					return String.format(
							"%s=USERNAME:PASSWORD",	params.getOption());
				}
				
			})
			.ordinal(3)
			.build();
	
	public static final Option HELP_OPTION = new GnuLongOption.Builder("help")
			.doc("Print this help and exit")
			.ordinal(4)
			.otherBuilders(new PosixOption.Builder('h'))
			.special(true)
			.build();
	
	public static final Option NEW_CONFIG_FILE_OPTION = 
			new GnuLongOption.Builder("new-config-file")
			.doc("Create a new configuration file based on the preceding "
					+ "options and exit")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("FILE")
					.build())
			.ordinal(5)
			.otherBuilders(new PosixOption.Builder('n'))
			.special(true)
			.build();
	
	public static final Option SETTINGS_HELP_OPTION = new GnuLongOption.Builder(
			"settings-help")
			.doc("Print the list of available settings for the SOCKS server "
					+ "and exit")
			.ordinal(6)
			.otherBuilders(new PosixOption.Builder('H'))
			.special(true)
			.build();
	
	public static final Option SETTINGS_OPTION = new GnuLongOption.Builder(
			"settings")
			.doc("The comma-separated list of settings for the SOCKS server")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("SETTINGS")
					.type(Settings.class)
					.build())
			.optionUsageProvider(new OptionUsageProvider() {

				@Override
				public String getOptionUsage(final OptionUsageParams params) {
					return String.format(
							"%1$s=[%2$s1=%3$s1[,%2$s2=%3$s2[...]]]", 
							params.getOption(),
							"NAME",
							"VALUE");
				}
				
			})
			.ordinal(7)
			.otherBuilders(new PosixOption.Builder('s')
					.optionUsageProvider(new OptionUsageProvider() {
						
						@Override
						public String getOptionUsage(
								final OptionUsageParams params) {
							return String.format(
									"%1$s [%2$s1=%3$s1[,%2$s2=%3$s2[...]]]",
									params.getOption(),
									"NAME",
									"VALUE");
						}
						
					}))
			.build();
	
	public static final Option SOCKS5_USER_PASS_AUTHENTICATOR_OPTION = 
			new GnuLongOption.Builder("socks5-user-pass-authenticator")
			.doc("The SOCKS5 username password authenticator for the SOCKS server")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("SOCKS5_USER_PASS_AUTHENTICATOR")
					.type(UsernamePasswordAuthenticator.class)
					.build())
			.optionUsageProvider(new OptionUsageProvider() {

				@Override
				public String getOptionUsage(final OptionUsageParams params) {
					return String.format(
							"%s=CLASSNAME[:PARAMETER_STRING]", 
							params.getOption());
				}
				
			})
			.ordinal(8)
			.build();
	
	public static final Option SOCKS5_USERS_OPTION = new GnuLongOption.Builder(
			"socks5-users")
			.doc(String.format("Mode for managing SOCKS5 users "
					+ "(add %s for more information)",
					jargyle.server.socks5.UsersCliOptions.HELP_OPTION.getUsage()))
			.ordinal(9)
			.special(true)
			.build();
	
	public SocksServerCliOptions() { }
	
}