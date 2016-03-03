/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, www.wuyushuo.com All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the wuyushuo developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class AgentFactory {

    private final static Map<String, List<String>> AGENT_MULTI_MAP = new ConcurrentHashMap<String, List<String>>();

    private static final Logger LOG = LoggerFactory.getLogger(AgentFactory.class);

    /**
     * line count
     */
    static class CounterLine implements LineProcessor<List<String>> {
        private List<String> lines = new ArrayList<String>(1000);
        @Override
        public boolean processLine(String line) throws IOException {
            if (!line.startsWith("#") && !Strings.isNullOrEmpty(line)) {
                lines.add(line);
            }
            return true;
        }
        @Override
        public List<String> getResult() {
            return lines;
        }
    }


    private AgentFactory() {
        initClientAgentPool();
        initMobileAgentPool();
    }

    private final static AgentFactory factory = new AgentFactory();

    public static final AgentFactory getInstance(){
        return factory;
    }

    private void initClientAgentPool(){
        initUserAgentPool(AgentType.CLIENT, AgentConf.CLIENT_ANGENT_FILE);
    }

    public void initMobileAgentPool(){
        initUserAgentPool(AgentType.MOBILE, AgentConf.MOBILE_AGENT_FILE);
    }

    public void initUserAgentPool(AgentType agentType, String agentFile) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(agentFile);
        CounterLine counter = new CounterLine();
        if(null != url){
            try {
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    List<String> agents = Files.readLines(new File(url.getFile()), Charsets.UTF_8, counter);
                    LOG.info("load agent size :" + agents.size());
                    AGENT_MULTI_MAP.put(agentType.name(), agents);
                } else if (protocol.equals("jar")) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(jarURLConnection.getInputStream()));
                    List<String> agents = new ArrayList<String>();
                    String line = null;
                    while (null!=(line = reader.readLine())){
                        if (!line.startsWith("#") && !Strings.isNullOrEmpty(line)) {
                            agents.add(line);
                        }
                    }
                    LOG.info("load agent size :" + agents.size());
                    AGENT_MULTI_MAP.put(agentType.name(), agents);
                }
            }catch (Exception e){
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * @param agentType mobile/client/all
     * @return
     */
    public String getUserAgent(AgentType agentType) {
        if (AgentType.CLIENT.name().equals(agentType.name())) {
            List<String> result = (List<String>) AGENT_MULTI_MAP.get(AgentType.CLIENT.name());
            return result.get(ThreadLocalRandom.current().nextInt(result.size()));
        }
        if (AgentType.MOBILE.name().equals(agentType.name())) {
            List<String> result = AGENT_MULTI_MAP.get(AgentType.MOBILE.name());
            return result.get(ThreadLocalRandom.current().nextInt(result.size()));
        }
        if (AgentType.RANDOM.name().equals(agentType.name())) {
            int x = ThreadLocalRandom.current().nextInt(0, 2);
            if (x == 0) {
                List<String> result = AGENT_MULTI_MAP.get(AgentType.CLIENT.name());
                return result.get(ThreadLocalRandom.current().nextInt(result.size()));
            } else {
                List<String> result = AGENT_MULTI_MAP.get(AgentType.MOBILE.name());
                return result.get(ThreadLocalRandom.current().nextInt(result.size()));
            }
        }
        return null;
    }

    public static void main(String[] args){
        System.out.println(AgentFactory.getInstance().getUserAgent(AgentType.RANDOM));
    }

}
