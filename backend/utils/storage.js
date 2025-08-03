import { CloudinaryStorage } from 'multer-storage-cloudinary';
import multer from 'multer';
import cloudinary from './cloudinary.js';

const storagePromo = new CloudinaryStorage({
  cloudinary: cloudinary,
  params: {
    folder: 'promo_slike',
    allowed_formats: ['jpg', 'png', 'jpeg']
  },
});

const storageEvent = new CloudinaryStorage({
  cloudinary: cloudinary,
  params: {
    folder: 'dogadjaji_slike',
    allowed_formats: ['jpg', 'png', 'jpeg']
  },
});

const storageHall = new CloudinaryStorage({
  cloudinary: cloudinary,
  params: {
    folder: 'sale_slike',
    allowed_formats: ['jpg', 'png', 'jpeg']
  },
});

const storageOffer = new CloudinaryStorage({
  cloudinary: cloudinary,
  params: {
    folder: 'ponude_slike',
    allowed_formats: ['jpg', 'png', 'jpeg']
  },
});


const uploadPromo = multer({ storage: storagePromo }); 
const uploadEvent = multer({storage: storageEvent})
const uploadHall = multer({storage: storageHall, limits: {
    fileSize: 50 * 1024 * 1024
}})
const uploadOffer = multer({storage: storageOffer})

export {uploadPromo, uploadEvent, uploadHall, uploadOffer}
