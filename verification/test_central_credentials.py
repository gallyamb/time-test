import os
import subprocess
import unittest
from pathlib import Path

SCRIPT = Path(__file__).resolve().parents[1] / ".github/scripts/select-central-credentials.sh"
KEYS = ("MAVEN_CENTRAL_USERNAME", "MAVEN_CENTRAL_PASSWORD", "OSSRH_USERNAME", "OSSRH_PASSWORD")


class CentralCredentialsTest(unittest.TestCase):
    def check_selection(self, values, expected=None):
        env = {k: v for k, v in os.environ.items() if k not in KEYS}
        env.update(values)
        code = 'source "$1" || exit $?; [[ "$ORG_GRADLE_PROJECT_mavenCentralUsername" == "$2" && "$ORG_GRADLE_PROJECT_mavenCentralPassword" == "$3" ]]'
        result = subprocess.run(["bash", "-c", code, "credentials-test", str(SCRIPT), *(expected or ("", ""))], env=env, capture_output=True, text=True)
        self.assertNotIn("synthetic-password", result.stdout + result.stderr)
        if expected is None:
            self.assertNotEqual(0, result.returncode)
            self.assertIn("Unable to select Central credentials:", result.stderr)
        else:
            self.assertEqual(0, result.returncode, result.stderr)

    def test_prefers_complete_portal_pair(self):
        self.check_selection(dict(zip(KEYS, ("portal", "synthetic-password", "legacy", "unused"))), ("portal", "synthetic-password"))

    def test_accepts_existing_secret_names_as_a_pair(self):
        self.check_selection({"OSSRH_USERNAME": "portal-under-old-name", "OSSRH_PASSWORD": "synthetic-password"}, ("portal-under-old-name", "synthetic-password"))

    def test_rejects_incomplete_portal_pair_without_mixing_pairs(self):
        self.check_selection({"MAVEN_CENTRAL_USERNAME": "partial", "OSSRH_USERNAME": "legacy", "OSSRH_PASSWORD": "synthetic-password"})

    def test_rejects_incomplete_legacy_pair(self):
        self.check_selection({"OSSRH_USERNAME": "partial"})

    def test_rejects_missing_credentials(self):
        self.check_selection({})


if __name__ == "__main__":
    unittest.main()
