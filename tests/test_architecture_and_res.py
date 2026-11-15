"""Tests verifying Android multilingual XML resources, domain layout, and container hardening."""

from __future__ import annotations

import unittest
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


class TestMultilingualResourcesAndInfra(unittest.TestCase):
    def test_localized_strings_xml_integrity(self) -> None:
        locales = {
            "values": "Salamo Alikom",
            "values-en": "Hello",
            "values-ar": "السلام عليكم",
        }
        res_dir = ROOT / "app" / "src" / "main" / "res"
        for folder, expected_msg in locales.items():
            xml_path = res_dir / folder / "strings.xml"
            self.assertTrue(xml_path.is_file(), f"Missing {xml_path}")
            root = ET.fromstring(xml_path.read_text(encoding="utf-8"))
            entries = {el.attrib.get("name"): (el.text or "").strip() for el in root.findall("string")}
            self.assertEqual(entries.get("app_message"), expected_msg)
            self.assertEqual(entries.get("app_name"), "TP1_ENIAD")

    def test_clean_architecture_java_domain_present(self) -> None:
        domain_dir = ROOT / "app" / "src" / "main" / "java" / "com" / "mohavic" / "tp1_eniad" / "domain"
        expected_files = [
            "SupportedLocale.java",
            "GreetingResolution.java",
            "LocalizationCatalog.java",
            "DefaultLocalizationCatalog.java",
            "TelemetryRecorder.java",
            "InMemoryTelemetryRecorder.java",
            "LocaleGreetingService.java",
            "ServiceLocator.java",
            "DomainHealthCheck.java",
        ]
        for filename in expected_files:
            self.assertTrue((domain_dir / filename).is_file(), f"Missing domain class {filename}")

    def test_hardened_dockerfile_and_dependabot(self) -> None:
        dockerfile = (ROOT / "Dockerfile").read_text(encoding="utf-8")
        self.assertIn("AS builder", dockerfile)
        self.assertIn("AS runtime", dockerfile)
        self.assertIn("USER 10001:10001", dockerfile)
        self.assertIn("HEALTHCHECK", dockerfile)

        dependabot = (ROOT / ".github" / "dependabot.yml").read_text(encoding="utf-8")
        self.assertIn("gradle", dependabot)
        self.assertIn("github-actions", dependabot)


if __name__ == "__main__":
    unittest.main()
