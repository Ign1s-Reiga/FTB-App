package dev.ftb.app.api.data.instances;

import dev.ftb.app.api.data.BaseData;
import dev.ftb.app.data.mod.CurseMetadata;
import dev.ftb.app.data.mod.ModInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class InstanceModsData extends BaseData {

    public UUID uuid;
    public boolean _private = false;

    public static class Reply extends InstanceModsData {

        public List<ModInfo> files;

        public Reply(InstanceModsData data, List<ModInfo> files) {
            type = "instanceModsReply";
            uuid = data.uuid;
            requestId = data.requestId;
            this.files = files;
        }
    }

    public static class RichModData extends InstanceModsData {
        public final ModInfo file;
        public final CurseMetadata richData;

        public RichModData(InstanceModsData data, ModInfo file, CurseMetadata richData) {
            type = "instanceModRichData";
            this.requestId = data.requestId;
            this.file = file;
            this.richData = richData;
        }
    }
    
    public static class AllRichModData extends InstanceModsData {
        public final List<RichModDataBinding> richModData;
        
        public AllRichModData(InstanceModsData data, List<RichModDataBinding> richModData) {
            type = "instanceAllModRichData";
            this.requestId = data.requestId;
            this.richModData = richModData;
        }
    }
    
    public record RichModDataBinding(
        ModInfo file,
        @Nullable CurseMetadata richData
    ) {} 

    public static class UpdateAvailable extends InstanceModsData {

        public final ModInfo file;
        public final CurseMetadata update;

        public UpdateAvailable(InstanceModsData data, ModInfo file, CurseMetadata update) {
            type = "instanceModUpdate";
            this.requestId = data.requestId;
            this.file = file;
            this.update = update;
        }
    }

    public static class UpdateCheckingFinished extends InstanceModsData {

        public UpdateCheckingFinished(InstanceModsData data) {
            type = "instanceModUpdateCheckingFinished";
            uuid = data.uuid;
            requestId = data.requestId;
        }
    }
}
