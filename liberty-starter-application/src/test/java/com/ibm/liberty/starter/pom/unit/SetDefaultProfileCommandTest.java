package com.ibm.liberty.starter.pom.unit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.liberty.starter.DomUtil;
import com.ibm.liberty.starter.ProjectZipConstructor.DeployType;
import com.ibm.liberty.starter.pom.SetDefaultProfileCommand;

public class SetDefaultProfileCommandTest {

    private Document pom;
    private Node localServerProfile;
    private Node bluemixProfile;

    @Before
    public void setupTemplatePom() throws ParserConfigurationException {
        pom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Node project = DomUtil.addChildNode(pom, pom, "project", null);
        Node profiles = DomUtil.addChildNode(pom, project, "profiles", null);
        localServerProfile = addProfile(pom, profiles, "localServer");
        bluemixProfile = addProfile(pom, profiles, "bluemix");
    }

    @Test
    public void canSetLocalAsDefaultProfile() throws Exception {
        SetDefaultProfileCommand testObject = new SetDefaultProfileCommand(DeployType.LOCAL);

        testObject.modifyPom(pom);

        assertProfileIsActiveByDefault(localServerProfile);
        assertProfileIsNotActiveByDefault(bluemixProfile);
    }

    @Test
    public void canSetBluemixAsDefaultProfile() throws Exception {
        SetDefaultProfileCommand testObject = new SetDefaultProfileCommand(DeployType.BLUEMIX);

        testObject.modifyPom(pom);
        
        assertProfileIsActiveByDefault(bluemixProfile);
        assertProfileIsNotActiveByDefault(localServerProfile);
    }

    private void assertProfileIsNotActiveByDefault(Node profile) {
        assertThat(profile.getChildNodes().getLength(), is(1));
    }

    private void assertProfileIsActiveByDefault(Node profile) {
        assertThat(profile.getChildNodes().getLength(), is(2));
        Node activationNode = DomUtil.getChildNode(profile, "activation", null);
        assertThat(activationNode, notNullValue());
        Node activeByDefaultNode = DomUtil.getChildNode(activationNode, "activeByDefault", "true");
        assertThat(activeByDefaultNode, notNullValue());
    }

    private Node addProfile(Document pom, Node profiles, String name) {
        Node profile = DomUtil.addChildNode(pom, profiles, "profile", null);
        Node id = DomUtil.addChildNode(pom, profile, "id", null);
        id.setTextContent(name);
        return profile;
    }
}