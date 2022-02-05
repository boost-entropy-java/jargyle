package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.Routes;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.Selector;

public abstract class RoutingRule extends Rule {

	public static abstract class Builder<B extends Builder<B, R>, R extends RoutingRule> 
		extends Rule.Builder<B, R> {
		
		public static interface Field<B extends Builder<B, R>, R extends RoutingRule> 
			extends Rule.Builder.Field<B, R> {
			
		}
		
		private final List<String> routeIds;
		private SelectionStrategy routeIdSelectionStrategy;
		
		public Builder() {
			this.routeIds = new ArrayList<String>();
			this.routeIdSelectionStrategy = null;
		}
		
		public B addRouteId(final String id) {
			this.routeIds.add(id);
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;			
		}

		public abstract R build();
		
		public B doc(final String d) {
			super.doc(d);
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;		
		}

		public B logAction(final LogAction lgAction) {
			super.logAction(lgAction);
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;
		}
		
		public final List<String> routeIds() {
			return Collections.unmodifiableList(this.routeIds);
		}
		
		public final SelectionStrategy routeIdSelectionStrategy() {
			return this.routeIdSelectionStrategy;
		}
		
		public B routeIdSelectionStrategy(
				final SelectionStrategy selectionStrategy) {
			this.routeIdSelectionStrategy = selectionStrategy;
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;
		}
		
	}
	
	public static abstract class Context extends Rule.Context {
		
		private Route route;
		private final Routes routes;
		
		public Context(final Routes rtes) {
			this.route = null;
			this.routes = rtes;
		}
		
		public final Route getRoute() {
			return this.route;
		}
		
		public final Routes getRoutes() {
			return this.routes;
		}

		public final void setRoute(final Route rte) {
			this.route = rte;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [routes=")
				.append(this.routes)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private final List<String> routeIds;
	private final SelectionStrategy routeIdSelectionStrategy;
	private final Selector<String> routeIdSelector;	
	
	protected RoutingRule(final Builder<?, ?> builder) {
		super(builder);
		List<String> ids = new ArrayList<String>(builder.routeIds);
		SelectionStrategy selectionStrategy = builder.routeIdSelectionStrategy;		
		this.routeIds = ids;
		this.routeIdSelectionStrategy = selectionStrategy;
		this.routeIdSelector = (selectionStrategy != null) ? 
				selectionStrategy.newSelector(ids) : SelectionStrategy.CYCLICAL.newSelector(ids);
	}
	
	public final List<String> getRouteIds() {
		return Collections.unmodifiableList(this.routeIds);
	}
	
	public final SelectionStrategy getRouteIdSelectionStrategy() {
		return this.routeIdSelectionStrategy;
	}

	protected final Selector<String> getRouteIdSelector() {
		return this.routeIdSelector;
	}
	
}
