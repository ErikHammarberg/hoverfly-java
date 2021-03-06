package io.specto.hoverfly.assertions;

import io.specto.hoverfly.junit.core.model.Response;
import org.assertj.core.api.AbstractAssert;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {
    public ResponseAssert(final Response actual) {
        super(actual, ResponseAssert.class);
    }


    public ResponseAssert hasStatus(final int status) {
        isNotNull();

        assertThat(actual.getStatus()).isEqualTo(status);

        return this;
    }

    public ResponseAssert hasNoBody() {
        isNotNull();

        assertThat(actual.getBody()).isEmpty();

        return this;
    }

    public ResponseAssert hasBody(final String body) {
        isNotNull();

        assertThat(actual.getBody()).isEqualTo(body);

        return this;
    }

    public ResponseAssert hasExactHeaders(final Header... headers) {
        isNotNull();

        final Set<Header> setOfHeaders = Arrays.stream(headers)
                .map(h -> Header.header(h.getKey(), h.getValue()))
                .collect(toSet());

        assertThat(actual.getHeaders()).containsOnly(setOfHeaders.toArray(new Header[setOfHeaders.size()]));

        return this;
    }

    public ResponseAssert hasNoHeaders() {
        isNotNull();

        assertThat(actual.getHeaders()).isEmpty();

        return this;
    }


}
