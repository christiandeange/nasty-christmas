const { logger } = require("firebase-functions");
const { onSchedule } = require("firebase-functions/v2/scheduler");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");

initializeApp();

exports.check = onSchedule("every 5 minutes", async (event) => {
    const firestore = getFirestore();

    // Expire game states older than 1 day.
    const expiresAt = new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString();
    const states = await firestore.collection("game-state")
      .where("updatedAt", "<=", expiresAt)
      .get();

    // Delete expired games in a single transaction.
    const batch = firestore.batch();
    let deleteCount = 0;
    states.docs.forEach(doc => {
        batch.delete(doc.ref);
        deleteCount++;
    });
    await batch.commit();

    if (deleteCount > 0) {
        logger.log(`Documents deleted successfully: ${deleteCount}.`);
    } else {
        logger.log("No documents to delete.")
    }
});
