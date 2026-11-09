import os
from pathlib import Path
import subprocess
import tempfile
import unittest

SCRIPT = Path(__file__).resolve().parents[1] / ".github/scripts/verify-release-tag.sh"

class ReleaseTagTest(unittest.TestCase):
    def test_requires_tag_even_when_same_named_branch_exists(self):
        with tempfile.TemporaryDirectory() as directory:
            def git(*args):
                subprocess.run(["git", "-C", directory, "-c", "user.name=Fixture",
                                "-c", "user.email=test@example.invalid", *args],
                               check=True, capture_output=True)
            git("init", "-b", "v0.1.1")
            git("commit", "--allow-empty", "-m", "first")
            env = dict(os.environ, RELEASE_TAG="v0.1.1", GITHUB_ENV=directory + "/env")
            def verify():
                return subprocess.run(["bash", str(SCRIPT)], cwd=directory,
                                      env=env, capture_output=True).returncode
            self.assertNotEqual(0, verify())
            git("tag", "v0.1.1")
            self.assertEqual(0, verify())
            self.assertIn("RELEASE_VERSION=0.1.1", Path(env["GITHUB_ENV"]).read_text())
            git("commit", "--allow-empty", "-m", "second")
            self.assertNotEqual(0, verify())
            git("tag", "-a", "v0.1.2", "-m", "annotated")
            env["RELEASE_TAG"] = "v0.1.2"
            self.assertEqual(0, verify())

if __name__ == "__main__":
    unittest.main()
