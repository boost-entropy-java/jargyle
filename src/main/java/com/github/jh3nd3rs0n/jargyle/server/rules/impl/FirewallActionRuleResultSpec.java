package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class FirewallActionRuleResultSpec 
	extends RuleResultSpec<FirewallAction> {

	public FirewallActionRuleResultSpec(final String s) {
		super(s, FirewallAction.class);
	}

	@Override
	public RuleResult<FirewallAction> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(FirewallAction.valueOfString(value));
	}

}
