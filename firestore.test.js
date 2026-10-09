const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const ADMIN_UID = "admin_789";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read words or user profiles", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("words").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: can read words", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com" }).firestore();
  await assertSucceeds(aliceDb.collection("words").get());
});

test("Non-admin user: cannot create or modify words", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com" }).firestore();
  await assertFails(aliceDb.collection("words").doc("word_1").set({
    id: "word_1",
    arabic: "كِتَابٌ",
    uzbek: "Kitob",
    transliteration: "Kitobun",
    category: "taom",
    level: "beginner"
  }));
});

test("Admin user (boborakhim3@gmail.com): can create words", async () => {
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, { email: "boborakhim3@gmail.com" }).firestore();
  await assertSucceeds(adminDb.collection("words").doc("word_1").set({
    id: "word_1",
    arabic: "كِتَابٌ",
    uzbek: "Kitob",
    transliteration: "Kitobun",
    category: "taom",
    level: "beginner"
  }));
});

test("User can create and read their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com" }).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).set({
    userId: ALICE_UID,
    email: "alice@test.com",
    displayName: "Alice",
    xp: 0,
    streak: 1,
    hearts: 5
  }));
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());
});

test("User cannot read another user's profile", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      email: "bob@test.com",
      displayName: "Bob",
      xp: 10,
      streak: 2,
      hearts: 5
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com" }).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
});

test("User can manage their own favorites and wrong_words", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com" }).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("favorites").doc("word_1").set({
    wordId: "word_1"
  }));
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("favorites").get());
});
