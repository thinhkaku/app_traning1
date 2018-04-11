package aero.pilotlog.models;

/**
 * Created by phuc.dd on 12/30/2016.
 */
public class SettingGeneralModel{
    public SettingGeneralModel(String settingGroup, String settingName) {
        this.settingGroup = settingGroup;
        this.settingName = settingName;
    }

    public String getSettingGroup() {
        return settingGroup;
    }

    public void setSettingGroup(String settingGroup) {
        this.settingGroup = settingGroup;
    }

    public String settingGroup;

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String settingName;
}
