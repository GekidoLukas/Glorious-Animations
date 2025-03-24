package net.gekidolukas.glorious_animations.interfaces;

public interface SwingTypeGetter {



    void setBlockBreakingTicks(int blockBreakingTicks);
    int getBlockBreakingTicks();

    void setInteractTicks(int afterInteractTicks);
    int getInteractTicks();

    void setDropTicks(int afterDropTicks);
    int getDropTicks();


    void setTotemTicks(int afterTotemTicks);
    int getTotemTicks();


    void setPostTridentThrowTicks(int postTridentThrowTicks);
    int getPostTridentThrowTicks();
}
