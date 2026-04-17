export const config = {
  runtime: 'edge'
};

const DAY_MS = 24 * 60 * 60 * 1000;

function json(data, status = 200) {
  return new Response(JSON.stringify(data), {
    status,
    headers: { 'content-type': 'application/json; charset=utf-8' }
  });
}

function normalizeCode(input) {
  return String(input || '').trim().toUpperCase();
}

function getRequestIp(req) {
  const forwarded = req.headers.get('x-forwarded-for');
  if (forwarded) return forwarded.split(',')[0].trim();
  return (req.headers.get('x-real-ip') || '').trim();
}

async function deleteClip(sbUrl, sbServiceKey, code) {
  await fetch(`${sbUrl}/rest/v1/clips?code=eq.${encodeURIComponent(code)}`, {
    method: 'DELETE',
    headers: {
      apikey: sbServiceKey,
      authorization: `Bearer ${sbServiceKey}`
    }
  });
}

function decodeEnvelope(content) {
  try {
    const parsed = JSON.parse(content);
    if (parsed && parsed.v === 2 && Object.hasOwn(parsed, 'payload')) {
      return {
        payload: parsed.payload,
        meta: parsed.meta && typeof parsed.meta === 'object' ? parsed.meta : {}
      };
    }
  } catch {}
  return { payload: content, meta: {} };
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
  if (!/^[A-Z0-9]{6}$/.test(code)) return json({ error: 'Invalid code' }, 400);

  const selectUrl = `${sbUrl}/rest/v1/clips?code=eq.${encodeURIComponent(code)}&select=code,content,type,created_at&limit=1`;
  const clipRes = await fetch(selectUrl, {
    headers: {
      apikey: sbServiceKey,
      authorization: `Bearer ${sbServiceKey}`,
      accept: 'application/json'
    }
  });

  if (!clipRes.ok) return json({ error: 'Failed to fetch clip' }, 500);
  const rows = await clipRes.json();
  const row = Array.isArray(rows) && rows.length ? rows[0] : null;
  if (!row) return json({ error: 'Code not found or expired' }, 404);

  const createdMs = new Date(row.created_at).getTime();
  if (Number.isNaN(createdMs) || Date.now() - createdMs > DAY_MS) {
    await deleteClip(sbUrl, sbServiceKey, code);
    return json({ error: 'Code not found or expired' }, 404);
  }

  const { payload, meta } = decodeEnvelope(row.content);
  const allowedIp = typeof meta.allowedIp === 'string' ? meta.allowedIp.trim() : '';
  if (allowedIp) {
    const requesterIp = getRequestIp(req);
    if (!requesterIp) return json({ error: 'Secure lock denied: requester IP unavailable' }, 403);
    if (requesterIp !== allowedIp) return json({ error: 'Secure lock denied for this IP' }, 403);
  }

  if (meta.burnAfterRead) await deleteClip(sbUrl, sbServiceKey, code);

  return json({
    ok: true,
    clip: {
      code: row.code,
      type: row.type,
      content: payload,
      created_at: row.created_at,
      meta: {
        burnAfterRead: !!meta.burnAfterRead,
        allowedIp,
        encrypted: !!meta.encrypted,
        encryption: meta.encryption && typeof meta.encryption === 'object' ? meta.encryption : null
      }
    }
  });
}
