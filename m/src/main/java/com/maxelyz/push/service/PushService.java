/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.push.service;

import com.maxelyz.push.model.value.Message;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.EventBus;

public interface PushService {
    void onOpen(RemoteEndpoint r, EventBus eventBus);
    void onClose(RemoteEndpoint r, EventBus eventBus);
    Message onMessage(Message message);
}
