package org.oss.focussnip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.oss.focussnip.model.SnapGoods;


public interface SnapService extends IService<SnapGoods> {
     public boolean initSnap();

}
