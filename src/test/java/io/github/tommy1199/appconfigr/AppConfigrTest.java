package io.github.tommy1199.appconfigr;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class AppConfigrTest {

    private File sampleConfigsDirectory;

    @Before
    public void setUp() throws Exception {
        sampleConfigsDirectory = new File(this.getClass()
                .getResource("/sample-configs")
                .getFile());
    }

    @Test
    public void shouldThrowNullPointerExIfGivenPathIsNull() {
        try {
            AppConfigr.fromDirectory((Path) null);
            fail("NullPointerException expected when calling fromDirectory with [null] parameter.");
        } catch (NullPointerException e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void shouldThrowNullPointerExIfGivenFileIsNull() {
        try {
            AppConfigr.fromDirectory((File) null);
            fail("NullPointerException expected when calling fromDirectory with [null] parameter.");
        } catch (NullPointerException e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void shouldThrowNullPointerExIfGivenStringPathIsNull() {
        try {
            AppConfigr.fromDirectory((String) null);
            fail("NullPointerException expected when calling fromDirectory with [null] parameter.");
        } catch (NullPointerException e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void shouldThrowIllegalArgumentExIfGivenPathIsNotARealDirectory() {
        try {
            AppConfigr.fromDirectory(Paths.get("unknown/folder"))
                    .build();
            fail("IllegalArgumentException expected when calling build with not existing directory.");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void shouldSuppressDirectoryCheckIfDeactivatedInBuilder() {
        AppConfigr.fromDirectory(Paths.get("unknown/folder"))
                .noCheck()
                .build();
    }

    @Test
    public void shouldBuildIfPathExist() {
        AppConfigr.fromDirectory(sampleConfigsDirectory.toPath())
                .build();
    }

    @Test
    public void shouldBuildIfDirFileExist() {
        AppConfigr.fromDirectory(sampleConfigsDirectory)
                .build();
    }

    @Test
    public void shouldBuildIfStringPathExist() {
        AppConfigr.fromDirectory(sampleConfigsDirectory.getAbsolutePath())
                .build();
    }

    @Test
    public void shouldLoadConfigWithNameDerivedFromClassName() {
        AppConfigr cut = AppConfigr.fromDirectory(sampleConfigsDirectory)
                .build();

        SampleConfig config = cut.getConfig(SampleConfig.class);

        assertThat(config.getSampleInt()).isEqualTo(12);
        assertThat(config.getSampleString()).isEqualTo("this is a string");
    }

    @Test
    public void shouldLoadConfigWithCustomName() {
        AppConfigr cut = AppConfigr.fromDirectory(sampleConfigsDirectory)
                .build();

        SampleConfig config = cut.getConfig(SampleConfig.class, "custom.conf");

        assertThat(config.getSampleInt()).isEqualTo(14);
        assertThat(config.getSampleString()).isEqualTo("this is another string");
    }

    @Test
    public void shouldLoadConfigWithAnotherFormat() {
        AppConfigr cut = AppConfigr.fromDirectory(sampleConfigsDirectory)
                .withFactory(new JsonFactory())
                .build();

        SampleConfig config = cut.getConfig(SampleConfig.class, "sample-config-in-json.conf");

        assertThat(config.getSampleInt()).isEqualTo(12);
        assertThat(config.getSampleString()).isEqualTo("this is a string");
    }
}