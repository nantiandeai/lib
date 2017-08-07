package com.lianyitech.modules.report.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.report.entity.BookCirculte;
import com.lianyitech.modules.report.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yangkai on 2016/11/1.
 */
@RestController
@RequestMapping(value = "/api/report/Time")
public class TimeController extends ApiController{

    @Autowired
    private TimeService timeService;
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findByBookCirculte(BookCirculte bookCirculte) {
        return new ResponseEntity<>(success(timeService.findByBookCirculte(bookCirculte)), HttpStatus.OK);
    }
}
