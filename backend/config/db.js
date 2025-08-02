const mongoose = require('mongoose');

const url = `mongodb://127.0.0.1:27017/TrenuciZaPamcenje`;

const connectDB = async () => {
    try {
        mongoose.connect(url)
        .then(() => {
            console.log('Connected to MongoDB!');
        })
        .catch((error) => {
            console.error('Error during connecting to MongoDB:', error);
        });
        const db = mongoose.connection;

        db.on('error', (error) => {
            console.error('Error during connecting to MongoDB: ', error);
        });

        db.once('open', () => {
            console.log('Status open');
        });

        db.on('disconnected', () => {
            console.log('Lost connection to MongoDB');
        });

    } catch (error) {
        console.error('Error during connecting to MongoDB:', error);
        process.exit(1);
    }
};

module.exports = connectDB;