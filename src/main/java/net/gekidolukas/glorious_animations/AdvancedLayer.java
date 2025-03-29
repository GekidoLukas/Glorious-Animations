package net.gekidolukas.glorious_animations;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;

public class AdvancedLayer {


    public AdvancedLayer () {

    }

    public ModifierLayer<IAnimation> animationContainer = new ModifierLayer<>();

    public MirrorModifier mirrorModifier = new MirrorModifier();
    public SpeedModifier speedModifier = new SpeedModifier();


    public KeyframeAnimation currentAnimation = null;
    public KeyframeAnimation prevAnimation = null;
    public String currentAnimationId = "";
    public String prevAnimationId = "";

    public int fadeTime = 0;
    public float animationSpeed = 1;
    public int priority = 0;
    public int prevPriority = 0;


    public void disableAnimation() {
        currentAnimation = CommonAnimations.BLANK_LOOP;
        currentAnimationId = "blank_loop";

    }

}
