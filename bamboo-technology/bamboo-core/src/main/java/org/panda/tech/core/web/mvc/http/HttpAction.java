package org.panda.tech.core.web.mvc.http;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

/**
 * HTTP行为结构
 */
public class HttpAction {

    private String uri;
    private HttpMethod method;

    public HttpAction(String uri) {
        this.uri = uri;
    }

    public HttpAction(String uri, HttpMethod method) {
        this.uri = uri;
        this.method = method;
    }

    public HttpAction(String uri, RequestMethod requestMethod) {
        this.uri = uri;
        this.method = HttpMethod.valueOf(requestMethod.name());
    }

    public String getUri() {
        return this.uri;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public RequestMethod getRequestMethod() {
        return this.method == null ? null : RequestMethod.valueOf(this.method.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpAction other = (HttpAction) o;
        return Objects.equals(this.uri, other.uri) && this.method == other.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uri, this.method);
    }

    @Override
    public String toString() {
        String method = this.method == null ? Strings.ASTERISK : this.method.name();
        return method + Strings.COLON + this.uri;
    }
}
