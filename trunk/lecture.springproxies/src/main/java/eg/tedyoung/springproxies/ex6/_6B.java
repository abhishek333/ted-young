package eg.tedyoung.springproxies.ex6;

import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

public class _6B {
	
	@PreAuthorize("hasRole('ADMIN')")
	public void adminOnly() {
	}
	
	@PreAuthorize("hasPermission(#document, 'WRITE')")
	public void writePermission(Document document) {
	}
	
	@PreAuthorize("#document.owner.name = principal.name")
	public void editDocumentOwnerOnly(Document document) {
	}
	
	@PostAuthorize("hasPermission(returnObject, 'WRITE')")
	public Document writePermission2() {
		return null;
	}
	
	@PostFilter("hasPermission(filterObject, 'READ')")
	public List<Document> findDocuments() {
		return null;
	}
	
	@PreFilter("hasPermission(filterObject, 'WRITE')")
	public void saveDocuments(List<Document> documents) {
	}
}
