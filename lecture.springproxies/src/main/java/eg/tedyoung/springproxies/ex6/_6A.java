package eg.tedyoung.springproxies.ex6;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class _6A {
	
	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED)
	public void transactionalMethod() {
	}
	
}
