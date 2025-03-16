package net.gekidolukas.sincereplayeranimations;

import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public interface torsoPosGetter {

    Vec3f getTorsoPos();

    Vec3f getTorsoRotation();

    void disableArms(boolean b);

    void disableLeftArmB(boolean b);

    void disableRightArmB(boolean b);

    void disableMainArmB(boolean b);

    void disableOffArmB(boolean b);

    void disableAnimationB(boolean b);

    void disableOverlayB(boolean b);

    void armPosMain(BipedEntityModel.ArmPose pos);

    void armPosOff(BipedEntityModel.ArmPose pos);


    int afterAttackTicks = 0;

    void setAfterAttackTicks(int ticks);

    int getAfterAttackTicks();
}

