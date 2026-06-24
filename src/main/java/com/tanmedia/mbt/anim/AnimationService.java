package com.tanmedia.mbt.anim;

/**
 * Animation service interface.
 */
public interface AnimationService {
    void initialize();
    void update(float deltaSeconds);
    void register(String id, Object anim);
    void unregister(String id);
}