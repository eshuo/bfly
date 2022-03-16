package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.service.AreaService;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class AreaHome extends EntityHome<Area> implements Serializable {
    private static final long serialVersionUID = -9028139079103290282L;
    @Resource
    private AreaService areaService;
    private Area selectedProvince;
    private Area selectedCity;
    private Area selectedCounty;
    private List<Area> allProvinces;
    private List<Area> allCities;
    private List<Area> allCounties;

    public void findAllCities() {
        allCounties = null;
        selectedCounty = null;
        if (selectedProvince == null) {
            allCities = null;
        } else {
            allCities = areaService.findAllCities(selectedProvince.getId());
            if (allCities.size() > 0) {
                if (selectedCity == null) {
                    selectedCity = allCities.get(0);
                    }
                findAllCounties();
            }
        }
    }

    public void findAllCounties() {
        selectedCounty = null;
        if (selectedCity == null) {
            allCounties = null;
        } else {
            allCounties = areaService.findAllCounties(selectedCity.getId());
        }
    }

    public List<Area> getAllCities() {
        return allCities;
    }

    public List<Area> getAllCounties() {
        return allCounties;
    }

    public List<Area> getAllProvinces() {
        if (allProvinces == null) {
            allProvinces = areaService.getAllProvinces();
            if (selectedProvince == null) {
                selectedProvince = allProvinces.get(0);
            }
            findAllCities();
            if (selectedCity == null) {
                selectedCity = allCities.get(0);
            }
            findAllCounties();
        }
        return allProvinces;
    }

    public Area getSelectedCity() {
        return selectedCity;
    }

    public Area getSelectedCounty() {
        return selectedCounty;
    }

    public Area getSelectedProvince() {
        return selectedProvince;
    }

    public void initByCity(Area area) {
        if (area != null) {
            selectedCity = area;
            allCounties = areaService.findAllCounties(getSelectedCity().getId());
            selectedProvince = getBaseService().get(Area.class, selectedCity.getParent().getId());
            allCities = areaService.findAllCities(getSelectedProvince().getId());
        }
    }

    /**
     * 通过城市名字初始化数据
     *
     * @param area
     */
    public void initByCityName(String area) {
        if (StringUtils.isNotEmpty(area)) {
            List<Area> areas = (List<Area>) getBaseService().find("from Area area where area.name=?", area);
            if (areas.size() > 0) {
                selectedCity = areas.get(0);
                allCounties = areaService.findAllCounties(getSelectedCity().getId());
                selectedProvince = getBaseService().get(Area.class, selectedCity.getParent().getId());
                allCities = areaService.findAllCities(getSelectedProvince().getId());
            }
        }
    }

    /**
     * 根据ID初始化数据
     *
     * @param areaId
     */
    public void initById(String areaId) {

        if (StringUtils.isNotEmpty(areaId)) {
            List<Area> areas = (List<Area>) getBaseService().find("from Area area where area.id=?", areaId);
            if (areas.size() > 0) {
                selectedCity = areas.get(0);
                allCounties = areaService.findAllCounties(getSelectedCity().getId());
                selectedProvince = getBaseService().get(Area.class, selectedCity.getParent().getId());
                allCities = areaService.findAllCities(getSelectedProvince().getId());
            }
        }
    }

    public void initByCounty(Area area) {
        if (area != null) {
            selectedCounty = area;
            selectedCity = getBaseService().get(Area.class, area.getParent().getId());
            allCounties = areaService.findAllCounties(getSelectedCity().getId());
            selectedProvince = getBaseService().get(Area.class, selectedCity.getParent().getId());
            allCities = areaService.findAllCities(getSelectedProvince().getId());
        }
    }

    public void setAllCities(List<Area> allCities) {
        this.allCities = allCities;
    }

    public void setAllCounties(List<Area> allCounties) {
        this.allCounties = allCounties;
    }

    public void setAllProvinces(List<Area> allProvinces) {
        this.allProvinces = allProvinces;
    }

    public void setSelectedCity(Area selectedCity) {
        this.selectedCity = selectedCity;
    }

    public void setSelectedCounty(Area selectedCounty) {
        this.selectedCounty = selectedCounty;
    }

    public void setSelectedProvince(Area selectedProvince) {
        this.selectedProvince = selectedProvince;
    }
}
