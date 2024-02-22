const { Storage } = require('@google-cloud/storage');
const express = require('express');
const cors = require('cors');
const { format } = require('util');
const Multer = require('multer');

const app = express();
const port = 5000;

const multer = Multer({
  storage: Multer.memoryStorage(),
  limits: {
    fileSize: 5 * 1024 * 1024,
  },
});

app.use(cors());

const cloudStorage = new Storage({
  keyFilename: 'communityapplication-e830a46949c8.json',
  projectId: 'communityapplication',
});

const bucketName = 'community-app';
const bucket = cloudStorage.bucket(bucketName);

app.post('/upload', multer.single('file'), function(req, res, next) {
  if (!req.file) {
    res.status(400).send('No file uploaded.');
    return;
  }
  const fileExtension = req.file.originalname.split('.').pop();
  const timestamp = Date.now();
  const fileName = `${req.file.originalname}_${timestamp}.${fileExtension}`;
  const blob = bucket.file(fileName);
  const blobStream = blob.createWriteStream();
  blobStream.on('error', (err) => {
    next(err);
  });
  blobStream.on('finish', () => {
    const publicUrl = format(`https://storage.googleapis.com/${bucket.name}/${blob.name}`);
    res.status(200).json({ publicUrl });
  });
  blobStream.end(req.file.buffer);
  console.log(req.file);
});

app.listen(port, () => {
  console.log(`listening at http://localhost:${port}`);
});