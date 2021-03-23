package nl.hu.cisq1.lingo.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class LingoArchitectureTest {
    public static String applicationPackages = "..application..";
    public static String dataPackages = "..data..";
    public static String domainPackages = "..domain.";
    public static String presentationPackages = "..presentation..";

    @Test
    @DisplayName("domain classes should only depend on domain classes")
    public void domainClassesShouldOnlyDependOnDomainClasses() {
        this.checkRule(
                classes()
                        .that().resideInAPackage(domainPackages)
                        .should().onlyHaveDependentClassesThat().resideInAPackage(domainPackages),
                this.importClasses(domainPackages)
        );
    }

    @Test
    @DisplayName("domain classes should only be accessed by the data, domain and application layer")
    public void domainClassesShouldOnlyBeAccessedByTheDataDomainAndApplicationLayer() {
        this.checkRule(
                classes()
                        .that().resideInAPackage(domainPackages)
                        .should().onlyBeAccessed().byAnyPackage(applicationPackages, dataPackages, domainPackages),
                this.importClasses(domainPackages)
        );
    }

    @Test
    @DisplayName("data classes should not be used by the presentation layer")
    public void dataClassesShouldNotBeUsedByThePresentationLayer() {
        this.checkRule(
                noClasses()
                        .that().resideInAPackage(presentationPackages)
                        .should().accessClassesThat().resideInAPackage(dataPackages),
                this.importClasses(dataPackages)
        );
    }

    @Test
    @DisplayName("data classes should not have any dependencies on other packages")
    public void dataClassesShouldNotHaveAnyDependenciesOnOtherPackages() {
        this.checkRule(
                noClasses()
                        .that().resideInAPackage(dataPackages)
                        .should().dependOnClassesThat()
                        .resideInAnyPackage(applicationPackages, domainPackages, presentationPackages),
                this.importClasses(dataPackages)
        );
    }

    @Test
    @DisplayName("application layer should only be used by presentation layer")
    public void applicationLayerShouldOnlyBeUsedByPresentationLayer() {
        this.checkRule(
                classes()
                        .that().resideInAPackage(applicationPackages)
                        .should().onlyBeAccessed().byAnyPackage(presentationPackages),
                this.importClasses(applicationPackages)
        );
    }

    @Test
    @DisplayName("other packages should not access presentation package")
    public void otherPackagesShouldNotAccessPresentationClasses() {
        this.checkRule(
                noClasses()
                        .that().resideInAnyPackage(applicationPackages, dataPackages, domainPackages)
                        .should().accessClassesThat().resideInAPackage(presentationPackages),
                this.importClasses(applicationPackages)
        );
    }

    private JavaClasses importClasses(String packageName) {
        return new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages(packageName);
    }

    private void checkRule(ClassesShouldConjunction rule, JavaClasses classes) {
        rule.check(classes);
    }
}
