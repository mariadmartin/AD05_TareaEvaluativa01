package eus.birt.dam.ad05.repository;

import java.util.List;

import eus.birt.dam.ad05.entity.Nota;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {

	List<Nota> findByCuadernoId(Long cuadernoId);
}
