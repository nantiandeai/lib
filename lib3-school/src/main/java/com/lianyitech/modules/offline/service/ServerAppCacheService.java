package com.lianyitech.modules.offline.service;


import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.offline.entity.CopyVo;
import com.lianyitech.modules.offline.entity.PeriodicalVo;
import com.lianyitech.modules.offline.entity.ReaderVo;
import com.lianyitech.modules.offline.entity.ServerCirculateLog;
import com.lianyitech.modules.offline.utils.OfflineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

import static com.lianyitech.core.config.ConfigurerConstants.REDIS_HOST;
import static com.lianyitech.core.config.ConfigurerConstants.REDIS_PORT;

@Service
public class ServerAppCacheService {

    static final String CACHE_UP_DATA_PREFIX = "update_";
    @Autowired
    JedisPool jedisPool;

    @Autowired
    private AbstractEnvironment environment;

    public void set(String key , String value){
        Jedis jedis = getJedis();
        jedis.set(key,value);
        jedis.close();
    }
    public void set(String key ,int seconds , String value){
        Jedis jedis = getJedis();
        jedis.setex(key,seconds,value);
        jedis.close();
    }
    public void delete(String key){
        Jedis jedis = getJedis();
        jedis.del(key);
        jedis.close();
    }

    public String get(String key) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        jedis.close();
        return value ;
    }

    public void setUpData(String orgId,String type,String id,String data){
        setData(orgId,type,id);
        set(CACHE_UP_DATA_PREFIX+id,data);
    }
    public void setCopyDataToRedis(Jedis jedis, String orgId,List<CopyVo> pageList){
        for(CopyVo vo : pageList){
            jedis.sadd(CACHE_UP_DATA_PREFIX  + orgId + "_2" , vo.getId());
            jedis.set(CACHE_UP_DATA_PREFIX+vo.getId(), OfflineUtils.parseCopyLine(vo, "@td@"));
        }
    }
    public void setReaderDataToRedis(Jedis jedis, String orgId,List<ReaderVo> pageList){
        for(ReaderVo vo : pageList){
            jedis.sadd(CACHE_UP_DATA_PREFIX  + orgId + "_1" , vo.getId());
            jedis.set(CACHE_UP_DATA_PREFIX+vo.getId(), OfflineUtils.parseReaderLine(vo, "@td@"));
        }
    }
    public void setSyncDataToRedis(Jedis jedis, String orgId,List<ServerCirculateLog> pageList){
        for(ServerCirculateLog vo : pageList){
            jedis.sadd(CACHE_UP_DATA_PREFIX  + orgId + "_3" , vo.getId());
            jedis.set(CACHE_UP_DATA_PREFIX+vo.getId(), OfflineUtils.parseSyncLine(vo, "@td@"));
        }
    }

    /**
     * 期刊数据加入到缓存
     * @param jedis redis
     * @param orgId 机构id
     * @param pageList 期刊分页集合
     */
    public void setPeriodicalToRedis(Jedis jedis, String orgId,List<PeriodicalVo> pageList){
        for(PeriodicalVo vo : pageList){
            jedis.sadd(CACHE_UP_DATA_PREFIX  + orgId + "_4" , vo.getId());
            jedis.set(CACHE_UP_DATA_PREFIX+vo.getId(), OfflineUtils.parsePeriodicalLine(vo, "@td@"));
        }
    }

    public List<String> getUpData(String orgId,String type,int length){
        Jedis jedis = getJedis();
        List<String> resultList = new ArrayList<>();
        for ( int i = 0 ; i < length ; i++) {
            String id = jedis.srandmember(CACHE_UP_DATA_PREFIX  + orgId + "_" + type);
            if(id==null) {
                break ;
            }
            String res = jedis.get(CACHE_UP_DATA_PREFIX+id);
            if(StringUtils.isNotBlank(res)) {
                resultList.add(res);
                jedis.del(CACHE_UP_DATA_PREFIX+id);
                jedis.srem(CACHE_UP_DATA_PREFIX  + orgId + "_" + type,id);
            }
        }
        jedis.close();
        return resultList;
    }


    public void setData(String orgId,String type,String value) {
        Jedis jedis = getJedis();
        jedis.sadd(CACHE_UP_DATA_PREFIX  + orgId + "_" + type , value);
        jedis.close();
    }


    public Long scard(String orgId,String type){
        Jedis jedis = getJedis();
        long length =  jedis.scard(CACHE_UP_DATA_PREFIX + orgId + "_" + type);
        jedis.close();
        return length;
    }

    public Jedis getJedis(){
        Jedis jedis = new Jedis(environment.getProperty(REDIS_HOST),Integer.valueOf(environment.getProperty(REDIS_PORT)));
        return jedis;
    }


    public void closeJedis(Jedis jedis){
        if(jedis.isConnected()){
            try{
                jedis.disconnect();
            }catch(Exception e){
                System.out.println("退出失败");
                e.printStackTrace();
            }

        }
        jedis.close();
    }


}
