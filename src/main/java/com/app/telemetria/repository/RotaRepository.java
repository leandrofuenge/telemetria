package com.app.telemetria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.telemetria.entity.Rota;

public interface RotaRepository extends JpaRepository<Rota, Long> {
}
