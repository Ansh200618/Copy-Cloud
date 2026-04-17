export const config = {
  runtime: 'edge'
};

function json(data, status = 200) {
  return new Response(JSON.stringify(data), {
    status,
    headers: { 'content-type': 'application/json; charset=utf-8' }
  });
}

function normalizeCode(input) {
  return String(input || '').trim().toUpperCase();
}

export default async function handler(req) {
  if (req.method !== 'POST') return json({ error: 'Method not allowed' }, 405);

  const sbUrl = process.env.SUPABASE_URL;
  const sbServiceKey = process.env.SUPABASE_SERVICE_ROLE_KEY;
  if (!sbUrl || !sbServiceKey) return json({ error: 'Server not configured' }, 500);

  let body;
  try {
    body = await req.json();
  } catch {
    return json({ error: 'Invalid JSON body' }, 400);
  }

  const code = normalizeCode(body?.code);
  const type = body?.type === 'file' ? 'file' : 'text';
  const payload = body?.content;
  const meta = body?.meta && typeof body.meta === 'object' ? body.meta : {};

  if (!/^[A-Z0-9]{6}$/.test(code)) return json({ error: 'Invalid code' }, 400);
  if (payload === undefined || payload === null) return json({ error: 'Missing content' }, 400);

  const envelope = {
    v: 2,
    payload,
    meta: {
      burnAfterRead: !!meta.burnAfterRead,
      allowedIp: typeof meta.allowedIp === 'string' ? meta.allowedIp.trim() : '',
      encrypted: !!meta.encrypted,
      encryption: meta.encryption && typeof meta.encryption === 'object' ? meta.encryption : null
    }
  };

  const insertPayload = {
    code,
    content: JSON.stringify(envelope),
    type,
    created_at: new Date().toISOString()
  };

  const res = await fetch(`${sbUrl}/rest/v1/clips`, {
    method: 'POST',
    headers: {
      apikey: sbServiceKey,
      authorization: `Bearer ${sbServiceKey}`,
      'content-type': 'application/json',
      prefer: 'resolution=merge-duplicates,return=representation'
    },
    body: JSON.stringify(insertPayload)
  });

  if (!res.ok) {
    const text = await res.text();
    return json({ error: 'Failed to create clip', details: text }, 500);
  }

  return json({ ok: true, code });
}
