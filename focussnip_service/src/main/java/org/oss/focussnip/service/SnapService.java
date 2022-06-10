package org.oss.focussnip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.oss.focussnip.model.SnapGoods;

import java.util.List;


public interface SnapService extends IService<SnapGoods> {
     public List<SnapGoods> initSnap();

}
