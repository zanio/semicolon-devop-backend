package com.semicolondevop.suite.client.developer.jenkins;

import com.google.common.base.Throwables;
import org.apache.commons.codec.Charsets;

import java.io.IOException;

import static org.jclouds.util.Strings2.toStringAndClose;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 30/10/2020 - 11:46 AM
 * @project com.semicolondevop.suite.client.developer in ds-suite
 */
public abstract class BasicConfig {
    public String payloadFromResource(String resource) {
        try {
            return new String(toStringAndClose(getClass().getResourceAsStream(resource)).getBytes(Charsets.UTF_8));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
