/**
 *
 */
package com.lianyitech.modules.sys.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.hibernate.validator.constraints.Length;

/**
 * 出版社管理Entity
 *
 * @author zengzy
 * @version 2016-08-31
 */
public class Publishing extends DataEntity<Publishing> {

    private static final long serialVersionUID = 1L;
    private String code;        // 出版社编号
    private String name;        // 出版社名称
    private String areCode;        // 区域编号
    private String areName;        // 区域名称

    //查询专用
    private String isbn;

    public Publishing() {
        super();
    }

    public Publishing(String id) {
        super(id);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreCode() {
        return areCode;
    }

    public void setAreCode(String areCode) {
        this.areCode = areCode;
    }

    public String getAreName() {
        return areName;
    }

    public void setAreName(String areName) {
        this.areName = areName;
    }

    //isbn系统目前都自动过滤-
    public String getIsbn() {
        return isbn != null ? isbn.replace("-", "") : isbn;
    }

    //isbn 系统目前自动过滤-
    public void setIsbn(String isbn) {
        this.isbn = isbn != null ? isbn.replace("-", "") : isbn;
    }

}