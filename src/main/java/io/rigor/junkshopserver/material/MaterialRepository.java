package io.rigor.junkshopserver.material;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface MaterialRepository extends CrudRepository<Material, String> {
}
