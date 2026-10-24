package ca.noxid.lab.discord;

import ca.noxid.lab.EditorConfigDialog;
import dev.firstdark.rpc.DiscordRpc;
import dev.firstdark.rpc.enums.ActivityType;
import dev.firstdark.rpc.enums.ErrorCode;
import dev.firstdark.rpc.exceptions.UnsupportedOsType;
import dev.firstdark.rpc.handlers.RPCEventHandler;
import dev.firstdark.rpc.models.DiscordRichPresence;
import dev.firstdark.rpc.models.User;

public class DiscordRpcHandler extends RPCEventHandler {
    private DiscordRpc rpc = new DiscordRpc();
    private String name = "Dog Studio";
    private String state = "Editing Cave Story maps...";
    private String details = "";
    private long startTimestamp = 0;
    private String largeImageKey = "dogstudio";
    private String largeImageText = "Dog Studio";
    private String smallImageKey = "";
    private String smallImageText = "";
    private ActivityType activityType = ActivityType.PLAYING;

    public DiscordRpcHandler() {
        //rpc.setDebugMode(true);
        try {
            if (EditorConfigDialog.isRPCEnabled()) {
                rpc.init("1563050087897104404", this, false);

            } else {
                rpc = null;
            }
        } catch (UnsupportedOsType e) {
            rpc = null;
        }
    }

    public void setName(String name) {
        this.name = name;
        setPresence();
    }

    public void setState(String state) {
        this.state = state;
        setPresence();
    }

    public void setDetails(String details) {
        this.details = details;
        setPresence();
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
        setPresence();
    }

    public void setLargeImageKey(String largeImageKey) {
        this.largeImageKey = largeImageKey;
        setPresence();
    }

    public void setLargeImageText(String largeImageText) {
        this.largeImageText = largeImageText;
        setPresence();
    }

    public void setSmallImageKey(String smallImageKey) {
        this.smallImageKey = smallImageKey;
        setPresence();
    }

    public void setSmallImageText(String smallImageText) {
        this.smallImageText = smallImageText;
        setPresence();
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
        setPresence();
    }

    public void setPresence() {
        if(rpc == null) return;
        DiscordRichPresence presence = DiscordRichPresence.builder()
                .name(name)
                .state(state)
                .details(details)
                .startTimestamp(startTimestamp)
                .largeImageKey(largeImageKey)
                .largeImageText(largeImageText)
                .smallImageKey(smallImageKey)
                .smallImageText(smallImageText)
                .activityType(activityType)
                .build();
        rpc.updatePresence(presence);
    }

    @Override
    public void ready(User user) {
        startTimestamp = System.currentTimeMillis();
        setPresence();
    }

    @Override
    public void disconnected(ErrorCode errorCode, String message) {
        //System.out.println("Disconnected " + errorCode + " - " + message);
    }

    @Override
    public void errored(ErrorCode errorCode, String message) {
        //System.out.println("Errored " + errorCode + " - " + message);
    }
};
