package io.onedev.server.search.entity.pack;

import io.onedev.server.model.Pack;
import io.onedev.server.util.criteria.Criteria;

import javax.persistence.criteria.*;

import static io.onedev.server.util.match.WildcardUtils.matchString;

public class IdCriteria extends Criteria<Pack> {

	private static final long serialVersionUID = 1L;

	private final String value;
	
	private final int operator;
	
	public IdCriteria(String value, int operator) {
		this.value = value;
		this.operator = operator;
	}

	@Override
	public Predicate getPredicate(CriteriaQuery<?> query, From<Pack, Pack> from, CriteriaBuilder builder) {
		Path<String> attribute = from.get(Pack.PROP_NAME);
		String normalized = value.toLowerCase().replace("*", "%");
		var predicate = builder.like(builder.lower(attribute), normalized);
		if (operator == PackQueryLexer.IsNot)
			predicate = builder.not(predicate);
		return predicate;
	}

	@Override
	public boolean matches(Pack pack) {
		String name = pack.getName();
		var matches = name != null && matchString(value.toLowerCase(), name.toLowerCase());
		if (operator == PackQueryLexer.IsNot)
			matches = !matches;
		return matches;
	}

	@Override
	public String toStringWithoutParens() {
		return quote(Pack.NAME_ID) + " " 
				+ PackQuery.getRuleName(operator) + " " 
				+ quote(value);
	}

}
