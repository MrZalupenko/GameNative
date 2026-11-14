package com.winlator.container

import com.winlator.core.ClosedSourceComponents
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ContainerDefaultDisplayServerTest {

    private val adrenoDefault =
        if (ClosedSourceComponents.GAMESCOPE_DRM) Container.DISPLAY_SERVER_GAMESCOPE else Container.DISPLAY_SERVER_X11

    @Test
    fun `adreno on API 29 or newer defaults to gamescope only when its DRM backend is shipped`() {
        assertEquals(adrenoDefault, Container.defaultDisplayServer(true, 29))
        assertEquals(adrenoDefault, Container.defaultDisplayServer(true, 36))
    }

    @Test
    fun `adreno below API 29 defaults to x11 because ASurfaceControl is missing`() {
        assertEquals(Container.DISPLAY_SERVER_X11, Container.defaultDisplayServer(true, 28))
        assertEquals(Container.DISPLAY_SERVER_X11, Container.defaultDisplayServer(true, 26))
    }

    @Test
    fun `non-adreno defaults to x11 at every API level`() {
        assertEquals(Container.DISPLAY_SERVER_X11, Container.defaultDisplayServer(false, 28))
        assertEquals(Container.DISPLAY_SERVER_X11, Container.defaultDisplayServer(false, 36))
    }

    @Test
    fun `gamescope support follows the ASurfaceControl API floor and the DRM backend`() {
        assertFalse(Container.isGamescopeSupported(28))
        assertEquals(ClosedSourceComponents.GAMESCOPE_DRM, Container.isGamescopeSupported(29))
    }

    @Test
    fun `a stored gamescope choice is treated as x11 without the DRM backend`() {
        assumeFalse(ClosedSourceComponents.GAMESCOPE_DRM)
        assertTrue(Container.isX11Display(Container.BIONIC, Container.DISPLAY_SERVER_GAMESCOPE))
    }
}
