require('dotenv').config();
const { SweetbookClient } = require('../index');

async function main() {
  console.log('API KEY exists:', !!process.env.SWEETBOOK_API_KEY);
  console.log('ENV:', process.env.SWEETBOOK_ENV);

  const client = new SweetbookClient({
    apiKey: process.env.SWEETBOOK_API_KEY,
    environment: process.env.SWEETBOOK_ENV || 'live',
  });

  // 가장 단순한 호출부터 시도
  const books = await client.books.list();
  console.log('connected:', true);
  console.log('books result:', books);
}

main().catch((err) => {
  console.error('connected:', false);
  console.error(err);
});