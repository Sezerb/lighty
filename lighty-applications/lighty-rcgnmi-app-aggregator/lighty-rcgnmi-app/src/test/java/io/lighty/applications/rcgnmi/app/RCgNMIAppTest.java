/*
 * Copyright (c) 2021 PANTHEON.tech s.r.o. All Rights Reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v10.html
 */

package io.lighty.applications.rcgnmi.app;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.beust.jcommander.ParameterException;
import com.google.common.util.concurrent.Futures;
import io.lighty.applications.rcgnmi.module.RcGnmiAppException;
import io.lighty.applications.rcgnmi.module.RcGnmiAppModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RCgNMIAppTest {

    @Test
    public void testStartWithDefaultConfiguration() throws RcGnmiAppException {
        final RCgNMIApp app = Mockito.spy(new RCgNMIApp());
        final RcGnmiAppModule appModule = Mockito.mock(RcGnmiAppModule.class);
        doReturn(Futures.immediateFuture(true)).when(appModule).start();
        doReturn(Futures.immediateFuture(true)).when(appModule).shutdown();
        doReturn(appModule).when(app).createRgnmiAppModule(any(), any(), eq(30), any());
        app.start(new String[]{});
        verify(app, Mockito.times(1)).createRgnmiAppModule(any(), any(), eq(30), any());
    }

    @Test
    public void testStartWithConfigFile() throws RcGnmiAppException {
        final RCgNMIApp app = Mockito.spy(new RCgNMIApp());
        final RcGnmiAppModule appModule = Mockito.mock(RcGnmiAppModule.class);
        doReturn(Futures.immediateFuture(true)).when(appModule).start();
        doReturn(Futures.immediateFuture(true)).when(appModule).shutdown();
        doReturn(appModule).when(app).createRgnmiAppModule(any(), any(), eq(90), any());
        app.start(new String[]{"-c", "src/main/resources/example-config/example_config.json", "-t", "90"});
        verify(app, Mockito.times(1)).createRgnmiAppModule(any(), any(), eq(90), any());
    }

    @Test
    public void testStartWithConfigFileNoSuchFile() throws RcGnmiAppException {
        final RCgNMIApp app = Mockito.spy(new RCgNMIApp());
        app.start(new String[]{"-c", "no_config.json"});
        verify(app, Mockito.times(0)).createRgnmiAppModule(any(), any(), eq(30), any());
    }

    @Test
    public void testStartWithWrongTimeOut() {
        final RCgNMIApp app = spy(new RCgNMIApp());
        verify(app, never()).createRgnmiAppModule(any(), any(), eq(30), any());
        assertThrows(ParameterException.class, () -> app.start(new String[]{"-t", "WRONG_TIME_OUT"}));
    }
}
