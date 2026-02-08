-- Database Migration for Device Targeting Feature
-- Run this in your Supabase SQL Editor

-- Add target_device column to clips table
ALTER TABLE clips ADD COLUMN IF NOT EXISTS target_device TEXT;

-- Add index for faster queries when filtering by device
CREATE INDEX IF NOT EXISTS idx_clips_target_device ON clips(target_device);

-- Add comment to explain the field
COMMENT ON COLUMN clips.target_device IS 'Optional 8-digit device code for device-specific content delivery';

-- Example queries:

-- Get all clips for a specific device or public clips
-- SELECT * FROM clips WHERE target_device IS NULL OR target_device = '12345678';

-- Get clip by code that matches current device
-- SELECT * FROM clips WHERE code = 'ABC123' AND (target_device IS NULL OR target_device = '12345678');
