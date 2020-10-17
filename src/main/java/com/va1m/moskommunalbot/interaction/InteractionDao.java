package com.va1m.moskommunalbot.interaction;

import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

/** Provides methods to store and read {@link InteractionContext} from database */
public class InteractionDao {

    /** The simplest in-memory database */
    @SuppressWarnings("java:S1149")
    private static final Map<Long, InteractionContext> MEM_DATABASE = new Hashtable<>();

    /** Reads an {@link InteractionContext} from database */
    public Optional<InteractionContext> read(long chatId) {
        return Optional.ofNullable(MEM_DATABASE.get(chatId));
    }

    /** Writes an {@link InteractionContext} to database */
    public void write(long chatId, InteractionContext interactionContext) {
        MEM_DATABASE.put(chatId, interactionContext);
    }
}
