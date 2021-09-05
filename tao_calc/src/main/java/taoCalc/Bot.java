/*
 * Copyright 2018 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package taoCalc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.JDA;

/**
 *
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class Bot
{
    private final EventWaiter waiter;
    private final ScheduledExecutorService threadpool;
    
    private boolean shuttingDown = false;
    private JDA jda;
    
    public Bot(EventWaiter waiter)
    {
        this.waiter = waiter;
        this.threadpool = Executors.newSingleThreadScheduledExecutor();
    }
    
    
    public EventWaiter getWaiter()
    {
        return waiter;
    }
    
    public ScheduledExecutorService getThreadpool()
    {
        return threadpool;
    }
    
    
    public JDA getJDA()
    {
        return jda;
    }
    
    public void shutdown()
    {
        if(shuttingDown)
            return;
        shuttingDown = true;
        threadpool.shutdownNow();
        if(jda.getStatus()!=JDA.Status.SHUTTING_DOWN)
        {
            jda.getGuilds().stream().forEach(g -> 
            {
               g.getId();
            });
            jda.shutdown();
        }
        System.exit(0);
    }

    public void setJDA(JDA jda)
    {
        this.jda = jda;
    }
    
}
