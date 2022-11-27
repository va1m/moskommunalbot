package com.va1m.moskommunalbot;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {StateProcessorsModule.class})
public interface BotFactory {

    MosKommunalBot bot();
}
