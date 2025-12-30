package ar.edu.utn.frba.dds.services.impl;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IpBlockingService {

    // Usamos un Set concurrente para que sea seguro con muchos usuarios a la vez
    private final Set<String> automaticBlacklist = ConcurrentHashMap.newKeySet();

    public void blockIp(String ip) {
        System.out.println("!!! ALERTA: Bloqueando IP automáticamente: " + ip);
        automaticBlacklist.add(ip);
    }

    public boolean isBlocked(String ip) {
        return automaticBlacklist.contains(ip);
    }

    // perdonar (desbloquear)
    public void unblockIp(String ip) {
        automaticBlacklist.remove(ip);
    }
}