package main.java;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MaSession {
    private String id;
    private Map<String, Object> attributes;
    private Set<String> initialKeys;

    public MaSession() {
        this.attributes = new HashMap<>();
        this.initialKeys = new HashSet<>();
    }

    public static MaSession from(HttpSession session) {
        MaSession maSession = new MaSession();
        if (session != null) {
            maSession.id = session.getId();
            session.getAttributeNames().asIterator().forEachRemaining(name -> {
                Object value = session.getAttribute(name);
                maSession.attributes.put(name, value);
                maSession.initialKeys.add(name);
            });
        }
        return maSession;
    }

    public void flushTo(HttpSession httpsession) {
        if (httpsession == null) return;

        for (String key : initialKeys) {
            if (!attributes.containsKey(key)) {
                httpsession.removeAttribute(key);
            }
        }

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            httpsession.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}