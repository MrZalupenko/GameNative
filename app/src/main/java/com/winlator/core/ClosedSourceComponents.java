package com.winlator.core;

/**
 * Availability of the closed-source GameNative binaries (see THIRD_PARTY_NOTICES).
 *
 * This build ships none of them and never downloads them, so every feature that cannot work
 * without one is switched off here. The constants are compile-time, which lets R8 drop the
 * unreachable code paths from release builds.
 */
public final class ClosedSourceComponents {

    /** {@code libredirect*.so}: LD_PRELOAD shim injected into every Wine / Proton process. */
    public static final boolean LIBREDIRECT = false;

    /** {@code libgamenative_drm.so}: userspace DRM backend of the gamescope display server. */
    public static final boolean GAMESCOPE_DRM = false;

    /**
     * {@code libsteambootstrap.so}, {@code steamhost-*.tzst} and {@code steam-token.tzst}: the
     * helpers behind every "Launch Steam Client" mode. The EA and Rockstar launcher stand-ins
     * ({@code eahost-*.tzst}, {@code rgschost-*.tzst}) are only ever fetched from those modes, so
     * this switch covers them as well.
     */
    public static final boolean STEAM_CLIENT_HOSTS = false;

    private ClosedSourceComponents() {}
}
